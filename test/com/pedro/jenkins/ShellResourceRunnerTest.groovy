package com.pedro.jenkins

import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertThrows
import static org.junit.jupiter.api.Assertions.assertTrue

class ShellResourceRunnerTest {

    @Test
    void shouldThrowWhenReturnStdoutAndReturnStatusAreBothTrue() {
        FakeSteps steps = new FakeSteps()
        ShellResourceRunner runner = new ShellResourceRunner(steps)

        IllegalArgumentException ex = assertThrows(IllegalArgumentException) {
            runner.run('scripts/helloWorld.sh', [returnStdout: true, returnStatus: true])
        }

        assertEquals('returnStdout and returnStatus cannot both be true', ex.message)
    }

    @Test
    void shouldReturnStdoutAndEchoWhenPrintStdoutIsEnabled() {
        FakeSteps steps = new FakeSteps(stdout: 'hello from script\n')
        ShellResourceRunner runner = new ShellResourceRunner(steps)

        Object result = runner.run('scripts/helloWorld.sh', [returnStdout: true, fileName: 'test.sh'])

        assertEquals('hello from script', result)
        assertTrue(steps.echoes.any { it?.toString() == 'hello from script' })
        assertTrue(steps.stringShellCalls.any { it?.toString().contains('chmod +x') && it?.toString().contains('test.sh') })
        assertTrue(steps.mapShellCalls.any { it.script?.toString() == './test.sh' && it.returnStdout == true })
        assertTrue(steps.stringShellCalls.any { it?.toString().contains('rm -f') && it?.toString().contains('test.sh') })
    }

    @Test
    void shouldRunWithTraceAndArgsAndEnvWhenConfigured() {
        FakeSteps steps = new FakeSteps(status: 7)
        ShellResourceRunner runner = new ShellResourceRunner(steps)

        Object result = runner.run('scripts/helloWorld.sh', [
                returnStatus: true,
                trace       : true,
                fileName    : 'trace.sh',
                args        : ['a b', 'c'],
                env         : [APP_ENV: 'dev'],
                cleanup     : false
        ])

        assertEquals(7, result)
        assertEquals('APP_ENV=dev', steps.withEnvCalls[0][0].toString())
        assertTrue(steps.mapShellCalls.any {
            it.returnStatus == true && it.script?.toString() == "bash -x './trace.sh' 'a b' 'c'"
        })
    }

    private static class FakeSteps {
        String stdout = ''
        int status = 0

        final List<String> echoes = []
        final List<String> stringShellCalls = []
        final List<Map> mapShellCalls = []
        final List<List<String>> withEnvCalls = []

        String libraryResource(String resourcePath) {
            return '#!/usr/bin/env bash\necho test\n'
        }

        void writeFile(Map args) {
            // No-op for unit testing command construction.
        }

        Object sh(String command) {
            stringShellCalls.add(command)
            return null
        }

        Object sh(Map args) {
            Map mapArgs = args
            mapShellCalls.add(mapArgs)
            if (mapArgs.returnStdout == true) {
                return stdout
            }
            if (mapArgs.returnStatus == true) {
                return status
            }
            return null
        }

        Object withEnv(List<String> envList, Closure<?> body) {
            withEnvCalls.add(envList)
            return body.call()
        }

        void echo(String message) {
            echoes.add(message)
        }
    }
}

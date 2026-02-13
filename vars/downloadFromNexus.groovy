def call(Map cfg = [:]) {
    def nexusUrl   = cfg.nexusUrl?.trim()
    def repo       = cfg.repo?.trim()
    def groupId    = cfg.groupId?.trim()
    def artifactId = cfg.artifactId?.trim()
    def version    = cfg.version?.trim()
    def credsId    = cfg.credsId?.trim()

    if (!nexusUrl || !repo || !groupId || !artifactId || !version || !credsId) {
        error("downloadFromNexus missing required args: nexusUrl, repo, groupId, artifactId, version, credsId")
    }

    def requestedFileName = cfg.fileName?.toString()?.trim()
    def dest = cfg.dest?.toString()
    if (!dest && !requestedFileName) {
        dest = "${artifactId}-${version}"
    }
    def groupPath = groupId.replace('.', '/')
    def fileName = requestedFileName
    if (!fileName) {
        def lastSlash = dest.lastIndexOf('/')
        fileName = (lastSlash > -1) ? dest.substring(lastSlash + 1) : dest
    }
    if (!dest) {
        dest = fileName
    }

    def url = "${nexusUrl}/repository/${repo}/${groupPath}/${artifactId}/${version}/${fileName}"

    echo "Downloading artifact from: ${url}"
    sh "mkdir -p \$(dirname '${dest}')"

    withCredentials([usernamePassword(credentialsId: credsId, usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
        withEnv(["NEXUS_DOWNLOAD_URL=${url}", "NEXUS_DEST=${dest}"]) {
            sh '''
              set -eu
              curl -fL -u "$NEXUS_USER:$NEXUS_PASS" -o "$NEXUS_DEST" "$NEXUS_DOWNLOAD_URL"
              ls -lh "$NEXUS_DEST"
            '''
        }
    }

    return dest
}

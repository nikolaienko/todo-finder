package ua.nure.nikolaienko
/**
 * Created by Vladyslav_Nikolaienk on 5/31/2016.
 */
class GitLoader {

    def clone(String url, String login, String password) {
        def sout = new StringBuffer(), serr = new StringBuffer()

        def command = formatCommand(login, password, url)
        def executor = command.execute()
        executor.consumeProcessOutput(sout, serr)
        executor.waitForOrKill(10000000)
    }

    private String formatCommand(String login, String password, String url) {
        'C:\\Program Files\\Git\\cmd\\git.exe clone https://' + login + ':' + password + '@' + 'github.com/' + login +'/'+ url + '.git'
    }
}

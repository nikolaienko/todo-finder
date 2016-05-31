package ua.nure.nikolaienko

import java.util.concurrent.atomic.AtomicInteger

import static groovy.io.FileType.FILES

/**
 * Created by Vladyslav_Nikolaienk on 5/31/2016.
 */
class CodeAnylizer {
    private static final String JAVA_EXT = '.java'
    private static String TODO_REG = '//TODO:'
    private File file;

    public List<TODO> analize(File file) {
        def javaSourceClasses = findJavaSourceClasses(file)
        findTODOComments(javaSourceClasses)
    }

    private List<TODO> findTODOComments(List<File> javaSourceClasses) {
        List<TODO> todos = []
        def codeAnalyzerClosure = getTODOFinderClosure(todos)
        javaSourceClasses.each {
            def index = new AtomicInteger(0)
            it?.eachLine {
                codeAnalyzerClosure.call(it, index)
            }
        }
        todos
    }

    private Closure getTODOFinderClosure(List<TODO> todos) {
        Closure closure = { String it, AtomicInteger index ->
            if (it.contains(TODO_REG)) {
                todos << new TODO(it.replaceAll(TODO_REG, '').trim(), index.get(), file.name.replaceAll(JAVA_EXT, ''))
            }
            index.incrementAndGet()
        }
        closure
    }

    private static List<File> findJavaSourceClasses(File file) {
        List<File> list = []
        file.
        file.eachFileRecurse(FILES) {
            if (it.name.endsWith(JAVA_EXT)) list << it
        }
        list
    }
}

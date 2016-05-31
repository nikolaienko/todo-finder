package ua.nure.nikolaienko

/**
 * Created by Vladyslav_Nikolaienk on 5/31/2016.
 */
class Main {

    public static void main(String[] args) {
//        def file = new File('A.java')
//        String regexp = "//TODO:"
//        int index = 0;
//
//        new CodeAnylizer().analize(new File('HadoopPart1/')).each {println(it.toString())}
        new GitLoader().clone("yaroot","nikolaienko","yavlad21")
//        todos.each { println it.toString() }
    }

}

package typingTutor;

import java.awt.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HungryWordMover extends Thread {
    private HungryWord myWord;
    private AtomicBoolean done;
    private AtomicBoolean pause;
    private Score score;
    private FallingWord[] fallingWords;
    CountDownLatch startLatch; //so all can start at once

    HungryWordMover( HungryWord word) {
        myWord = word;
    }

    HungryWordMover( HungryWord word,FallingWord[] fallingWords,WordDictionary dict, Score score,
               CountDownLatch startLatch, AtomicBoolean d, AtomicBoolean p) {
        this(word);
        this.startLatch = startLatch;
        this.score=score;
        this.done=d;
        this.pause=p;
        this.fallingWords=fallingWords;
    }



    public void run() {

        //System.out.println(myWord.getWord() + " falling speed = " + myWord.getSpeed());
        try {
            System.out.println(myWord.getWord() + " waiting to start " );
            startLatch.await();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } //wait for other threads to start
        System.out.println(myWord.getWord() + " started" );
        while (!done.get()) {
            //animate the word
            while (!myWord.dropped() && !done.get()) {
                myWord.drop(10);
                for(int i=0;i<fallingWords.length;i++){
                    if(myWord.getX() < fallingWords[i].getX() + fallingWords[i].getWidth() &&
                            myWord.getX() +myWord.getWidth() > fallingWords[i].getX() &&
                            myWord.getY() < fallingWords[i].getY() + fallingWords[i].getHeight() &&
                            myWord.getY() + myWord.getHeight() > fallingWords[i].getY())
                    {
                        fallingWords[i].resetWord();
                        score.missedWord();
                    }
                }
                try {
                    sleep(myWord.getSpeed());
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                };
                while(pause.get()&&!done.get()) {};
            }
            if (!done.get() && myWord.dropped()) {
                score.missedWord();
                myWord.resetWord();
            }
            myWord.resetWord();
        }
    }

}

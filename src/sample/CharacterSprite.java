package sample;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 人物类
 */
public class CharacterSprite extends Parent {
    private enum Direction {
        Left, Right, Up, Down
    }
    private Direction direction = Direction.Left;
    private Direction lastDirection;
    private int x, y, width, height;
    private int index = 0;
    private int indexDiv = 5;
    private ImageView mImageView;
    private int speed = 5;

    //定时器
    private TimerTask timerTask = null;
    private Timer timer;

    private List<KeyCode> directions = new ArrayList<>();

    private CharacterListener characterListener;

    private int xLength = 41;
    private int yLength = 42;


    public CharacterSprite(CharacterListener characterListener,int x, int y, int width, int height, String url) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.characterListener = characterListener;
        Image actor = new Image(getClass().getResourceAsStream(url));
        mImageView = new ImageView(actor);
        mImageView.setViewport(new Rectangle2D(0, 0, width, height));
        mImageView.setLayoutX(x);
        mImageView.setLayoutY(y);
        getChildren().add(mImageView);

        if (characterListener!=null){
            //增加Timer
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //更新JavaFX的主线程的代码放在此处
                            int size = directions.size();
                            if (size>0){
                                KeyCode keyCode = directions.get(size-1);
                                if(keyCode == KeyCode.LEFT){
                                    moveLeft();
                                }else if(keyCode == KeyCode.RIGHT){
                                    moveRight();
                                }else if(keyCode == KeyCode.UP){
                                    moveUp();
                                }else if(keyCode == KeyCode.DOWN){
                                    moveDown();
                                }
                                characterListener.onMoved(keyCode);
                            }else {
                                stop();
                            }
                        }
                    });
                }
            };
            timer = new Timer();
            //首次执行的时候的延时
            long delay = 0;
            //每次执行的时候的时延
            long intevalPeriod = 30;
            // schedules the task to be run in an interval
            timer.scheduleAtFixedRate(timerTask, delay,
                    intevalPeriod);
        }

    }

    /**
     * 像下移动
     */
    public void moveDown() {
        direction = Direction.Down;
        if(lastDirection != direction){
            index = 0;
        }
        index++;
        if (index / indexDiv > 3) {
            index = 0;
        }
        mImageView.setViewport(new Rectangle2D(((index / indexDiv) % 4) * width, ((index / indexDiv) / 4) * height, width,
                height));


        mImageView.setLayoutY(mImageView.getLayoutY() + speed);
        y = y + speed;


        lastDirection = direction;
    }

    /**
     * 像左移动
     */
    public void moveLeft() {
        direction = Direction.Left;
        if(lastDirection != direction){
            index = 4 * indexDiv;
        }
        index++;
        if (index / indexDiv > 7) {
            index = 4 * indexDiv;
        }
        mImageView.setViewport(new Rectangle2D(((index / indexDiv) % 4) * width, ((index / indexDiv) / 4) * height, width,
                height));


        mImageView.setLayoutX(mImageView.getLayoutX() - speed);
        x = x - speed;

        lastDirection = direction;

    }

    /**
     * 像右移动
     */
    public void moveRight() {
        direction = Direction.Right;
        if(lastDirection != direction){
            index = 8 * indexDiv;
        }
        index++;
        if (index / indexDiv > 11) {
            index = 8 * indexDiv;
        }
        mImageView.setViewport(new Rectangle2D(((index / indexDiv) % 4) * width, ((index / indexDiv) / 4) * height, width,
                height));


        mImageView.setLayoutX(mImageView.getLayoutX() + speed);
        x = x + speed;

        lastDirection = direction;
    }

    /**
     * 像上移动
     */
    public void moveUp() {
        direction = Direction.Up;
        if(lastDirection != direction){
            index = 12 * indexDiv;
        }
        index++;
        if (index / indexDiv > 15) {
            index = 12 * indexDiv;
        }
        mImageView.setViewport(new Rectangle2D(((index / indexDiv) % 4) * width, ((index / indexDiv) / 4) * height, width,
                height));


        mImageView.setLayoutY(mImageView.getLayoutY() - speed);
        y = y - speed;

        lastDirection = direction;
    }

    /**
     * 人物停止
     * */
    public void stop(){
        if (lastDirection == Direction.Down){
            mImageView.setViewport(new Rectangle2D(0 * width,0 * height, width,
                    height));
        }else {
            if (lastDirection == Direction.Left){
                mImageView.setViewport(new Rectangle2D(0 * width,1 * height, width,
                        height));
            }else {
                if (lastDirection == Direction.Right){
                    mImageView.setViewport(new Rectangle2D(0 * width,2 * height, width,
                            height));
                }else {
                    if (lastDirection == Direction.Up){
                        mImageView.setViewport(new Rectangle2D(0 * width,3 * height, width,
                                height));
                    }
                }
            }
        }
    }

    public void addKeyCode(KeyCode keyCode){

        directions.remove(keyCode);
        directions.add(keyCode);
    }

    public void removeKeyCode(KeyCode keyCode){
        try{
            directions.remove(keyCode);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public void setLayout(int x,int y){
        this.x = x;
        this.y = y;
        mImageView.setLayoutX(x);
        mImageView.setLayoutY(y);
    }

}

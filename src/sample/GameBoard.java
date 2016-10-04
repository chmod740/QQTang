package sample;

import javafx.scene.input.KeyCode;

/**
 * 游戏盘,精灵控制器
 * @author HUPENG
 */
public class GameBoard {
    /**
     * 游戏过程监听器
     * */
    private GameListener gameListener;

    /**
     * 判断是否是服务器
     * */
    private boolean isServer;

    /**
     * 自己的精灵
     * */
    private CharacterSprite myCharacterSprite;


    /**
     * 对手的精灵
     * */
    private CharacterSprite opponentCharacterSprite;


    public GameBoard(GameListener gameListener, boolean isServer){
        this.gameListener = gameListener;
        this.isServer = isServer;
        if (isServer){
            myCharacterSprite = new CharacterSprite(50,50,100,100,"img/server_actor.png");
            opponentCharacterSprite = new CharacterSprite(400,50,100,100,"img/client_actor.png");
        }else{
            opponentCharacterSprite = new CharacterSprite(50,50,100,100,"img/server_actor.png");
            myCharacterSprite = new CharacterSprite(400,50,100,100,"img/client_actor.png");
        }
        gameListener.OnMyCharacterSpriteCreated(myCharacterSprite);
        gameListener.OnOpponentCharacterSpriteCreated(opponentCharacterSprite);
    }

    public void addLocalKeyCode(KeyCode keyCode){
        myCharacterSprite.addKeyCode(keyCode);
    }

    public void addRemoteKeyCode(KeyCode keyCode) {
        opponentCharacterSprite.addKeyCode(keyCode);
    }
    public void removeLocalKeyCode(KeyCode keyCode){
        myCharacterSprite.removeKeyCode(keyCode);
    }

    public void removeRemoteKeyCode(KeyCode keyCode){
        opponentCharacterSprite.removeKeyCode(keyCode);
    }

    /**
     * 取消精灵的定时器
     * */
    public void cancelCharacterSpriteTimerTask(){
        myCharacterSprite.cancelTimerTask();
    }


}

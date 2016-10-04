package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.apache.mina.core.session.IoSession;
import sample.mina.MinaUtil;
import sample.mina.MyData;
import sample.mina.SimpleMinaListener;

import javax.swing.*;
import java.net.InetAddress;

/**
 * Created by HUPENG on 2016/10/3.
 */
public class GamePanel extends Parent {
    /**
     * 服务器标识
     * */
    private boolean isServer = false;
    private boolean modeSelected = false;

    private GameBoard gameBoard = null;

    private MyGameListener myGameListener = null;

    /**
     *
     * */
    private MinaUtil minaUtil = null;

    /**
     * 初始化
     * */
    public void initial(){
        //初始化菜单
        MenuBar menuBar = new MenuBar();
        Menu settingMenu = new Menu("功能");
        Menu helpMenu = new Menu("帮助");
        MenuItem inviteItem = new MenuItem("邀请他人");
        MenuItem acceptItem = new MenuItem("接受邀请");
        MenuItem exitItem = new MenuItem("退出");
        MenuItem aboutItem = new MenuItem("关于");
        settingMenu.getItems().addAll(inviteItem, acceptItem, exitItem);
        helpMenu.getItems().add(aboutItem);
        menuBar.getMenus().addAll(settingMenu, helpMenu);
        getChildren().addAll(menuBar);

        exitItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        acceptItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                acceptInvite();
            }
        });

        inviteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                inviteOther();
            }
        });

        aboutItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                abputAuthor();
            }
        });

        //初始化游戏监听器
        myGameListener = new MyGameListener();




        Scene scene = getScene();
        //设置键盘键盘监听
        getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                try {
                    gameBoard.addLocalKeyCode(event.getCode());
                }catch (Exception e){

                }
                try {
                    MyData myData = new MyData();
                    myData.mode = Tool.getModeFromKeyCode(event.getCode(),true);
                    minaUtil.send(myData);
                }catch (Exception e){

                }
            }
        });
        getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                try {
                    gameBoard.removeLocalKeyCode(event.getCode());
                }catch (Exception e){

                }
                try {
                    MyData myData = new MyData();
                    myData.mode = Tool.getModeFromKeyCode(event.getCode(),false);
                    minaUtil.send(myData);
                }catch (Exception e){

                }
            }
        });
    }


    /**
     * 邀请别人
     * */
    private void inviteOther(){
        if(!modeSelected){
            modeSelected = true;
            isServer = true;
            try {
                minaUtil = MinaUtil.getInstance(new MySimpleMinaListener(),isServer,null);
                JOptionPane.showMessageDialog(null,
                        "你的IP地址为：" + InetAddress.getLocalHost().getHostAddress() ,"邀请别人", JOptionPane.INFORMATION_MESSAGE);
            }catch (Exception e){
                JOptionPane.showMessageDialog(null,
                        "获取Ip地址发生异常" ,"邀请别人", JOptionPane.INFORMATION_MESSAGE);
                e.printStackTrace();
            }
            //初始化网络监听
        }else {
            JOptionPane.showMessageDialog(null,
                    "游戏模式一旦选择无法修改，请重启游戏修改" ,"游戏提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * 接受邀请
     * */
    private void acceptInvite(){
        if(!modeSelected){
            String ipAddr = JOptionPane.showInputDialog("请输入邀请方IP地址：");
            if (!Tool.ipCheck(ipAddr)){
                JOptionPane.showMessageDialog(null,
                        "IP地址格式错误" ,"接受邀请", JOptionPane.INFORMATION_MESSAGE);
            }else {
                modeSelected = false;
                isServer = false;
                minaUtil = MinaUtil.getInstance(new MySimpleMinaListener(),isServer,ipAddr);
            }
        }else {
            JOptionPane.showMessageDialog(null,
                    "游戏模式一旦选择无法修改，请重启游戏修改" ,"游戏提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * 关于我们
     * */
    private void abputAuthor(){
        JOptionPane.showMessageDialog(null,
                "作者：胡鹏\n邮箱：545061225@163.com\n" ,"关于作者", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 窗口关闭事件
     * */
    public void stageCloseRequest(){
        try {
            gameBoard.cancelCharacterSpriteTimerTask();
        }catch (Exception e){

        }
    }

    /**
     * GamePanel与Map之间的回调
     * */
    class MyGameListener implements GameListener {
        @Override
        public void OnMyCharacterSpriteCreated(CharacterSprite characterSprite) {
            getChildren().add(characterSprite);
        }

        @Override
        public void OnOpponentCharacterSpriteCreated(CharacterSprite characterSprite) {
            getChildren().add(characterSprite);
        }
    }


    /**
     * 网络回调
     * */
    class MySimpleMinaListener implements SimpleMinaListener{

        @Override
        public void onReceive(Object obj, IoSession ioSession) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (obj instanceof MyData){
                        try {
                            MyData myData = (MyData)obj;
                            KeyCode keyCode = Tool.getKeyCodeFromMode(myData.mode);
                            if (myData.mode <= 4){
                                gameBoard.addRemoteKeyCode(keyCode);
                            }else{
                                gameBoard.removeRemoteKeyCode(keyCode);
                            }
                        }catch (Exception e){

                        }
                    }
                }
            });

        }

        @Override
        public void onLine(String msg) {
            //初始化游戏盘
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    gameBoard = new GameBoard(myGameListener, isServer);
                    JOptionPane.showMessageDialog(null,
                            msg ,"游戏提示", JOptionPane.INFORMATION_MESSAGE);
                }
            });

        }

        @Override
        public void offLine() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(null,
                            "对方已下线" ,"游戏提示", JOptionPane.INFORMATION_MESSAGE);
                }
            });

        }
    }

}

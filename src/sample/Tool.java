package sample;

import javafx.scene.input.KeyCode;

public class Tool {
    /**
     * 判断IP地址的合法性，这里采用了正则表达式的方法来判断
     * return true，合法
     * */
    public static boolean ipCheck(String text) {
        if (text != null && !text.isEmpty()) {
            // 定义正则表达式
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            // 判断ip地址是否与正则表达式匹配
            if (text.matches(regex)) {
                // 返回判断信息
                return true;
            } else {
                // 返回判断信息
                return false;
            }
        }
        return false;
    }

    /**
     * 由于网络传输中我的协议中只发送三个INT,因此需要做INT与keycode之间的映射
     * */
    public static KeyCode getKeyCodeFromMode(int mode){
        switch (mode){
            case 1:case 5:
                return KeyCode.UP;
            case 2:case 6:
                return KeyCode.DOWN;
            case 3:case 7:
                return KeyCode.LEFT;
            case 4:case 8:
                return KeyCode.RIGHT;
        }
        return KeyCode.SPACE;
    }

    public static int getModeFromKeyCode(KeyCode keyCode,boolean pressed){
        int temp = 0;
        if (keyCode == KeyCode.UP){
            temp = 1;
        }
        if (keyCode == KeyCode.DOWN){
            temp = 2;
        }
        if (keyCode == KeyCode.LEFT){
            temp = 3;
        }
        if (keyCode == KeyCode.RIGHT){
            temp = 4;
        }
        if (!pressed){
            temp = temp + 4;
        }
        return temp;
    }
}

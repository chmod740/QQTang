package sample;

/**
 * GamePanel与地图之间的回调监听
 * @author HUPENG
 */
public interface GameListener {
    public void OnMyCharacterSpriteCreated(CharacterSprite characterSprite);
    public void OnOpponentCharacterSpriteCreated(CharacterSprite characterSprite);
}

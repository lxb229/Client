
const { ccclass, property } = cc._decorator;
import * as dd from './../../Modules/ModuleManager';
@ccclass
export default class Game_Setting extends cc.Component {

    @property([cc.SpriteFrame])
    img_onOff_list: cc.SpriteFrame[] = [];

    @property(cc.Sprite)
    effect_img: cc.Sprite = null;

    @property(cc.Sprite)
    music_img: cc.Sprite = null;
    /**
     * 注销游戏
     * 
     * @type {cc.Node}
     * @memberof Game_Setting
     */
    @property(cc.Node)
    btn_logout: cc.Node = null;

    onLoad() {
        this.node.on("touchend", (event: cc.Event.EventTouch) => {
            event.stopPropagation();
            dd.ui_manager.isShowPopup = true;
            this.node.removeFromParent(true);
            this.node.destroy();
        }, this);

        this.effect_img.spriteFrame = dd.mp_manager.audioSetting.isEffect === true ? this.img_onOff_list[0] : this.img_onOff_list[1];
        this.music_img.spriteFrame = dd.mp_manager.audioSetting.isMusic === true ? this.img_onOff_list[0] : this.img_onOff_list[1];
    }

    /**
     * 点击音效按钮
     * 
     * @memberof Game_Setting
     */
    click_btn_effect() {
        dd.mp_manager.playButton();
        dd.mp_manager.audioSetting.isEffect = !dd.mp_manager.audioSetting.isEffect;
        dd.mp_manager.saveMPSetting();
        this.effect_img.spriteFrame = dd.mp_manager.audioSetting.isEffect === true ? this.img_onOff_list[0] : this.img_onOff_list[1];
    }

    /**
     * 点击音乐按钮
     * 
     * @memberof Game_Setting
     */
    click_btn_music() {
        dd.mp_manager.playButton();
        dd.mp_manager.audioSetting.isMusic = !dd.mp_manager.audioSetting.isMusic;
        dd.mp_manager.saveMPSetting();
        if (!dd.mp_manager.audioSetting.isMusic) {
            dd.mp_manager.stopBackGround();
        } else {
            dd.mp_manager.playBackGround();
        }
        this.music_img.spriteFrame = dd.mp_manager.audioSetting.isMusic === true ? this.img_onOff_list[0] : this.img_onOff_list[1];
    }

    /**
    * 点击退出游戏
    * 
    * @memberof Setting
    */
    click_btn_logout() {
        dd.mp_manager.playButton();
        if (dd.ui_manager.showLoading('正在注销，请稍后')) {
            let obj = { 'accountId': dd.ud_manager.mineData.accountId };
            let msg = JSON.stringify(obj);
            dd.ws_manager.sendMsg(dd.protocol.ACCOUNT_LOGIN_OUT, msg, (flag: number, content?: any) => {
                dd.ws_manager.disconnect(() => {
                    dd.destroy();
                    cc.sys.garbageCollect();
                    cc.game.restart();
                });
            });
        }
    }
}

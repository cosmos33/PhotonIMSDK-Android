package com.cosmos.photonim.imbase.chat.emoji;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.utils.LogUtils;
import com.cosmos.photonim.imbase.utils.Utils;
import com.cosmos.photonim.imbase.utils.task.TaskExecutor;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiUtils {
    private static final String TAG = "EmojiUtils";
    public static final String EMOJI_MATCH = "\\[.{1,3}\\]";
    private static volatile Map<String, String> emojiMap;
    private static List<EmojiJson.EmojiBean> emojiBeans;
    private static volatile boolean init = false;

    private EmojiUtils() {
    }

    public static class EmjiUtilsHolder {
        static EmojiUtils emojiUtils = new EmojiUtils();
    }

    public static EmojiUtils getInstance() {
        return EmjiUtilsHolder.emojiUtils;
    }

    private static final String emojiJson = "{\"emojis\":[\n" +
            "        { \"credentialName\" : \"[龇牙]\" ,\"resId\":\"emoji_ciya\"},\n" +
            "        { \"credentialName\" : \"[调皮]\" ,\"resId\":\"emoji_tiaopi\"},\n" +
            "        { \"credentialName\" : \"[流汗]\" ,\"resId\":\"emoji_liuhan\"},\n" +
            "        { \"credentialName\" : \"[偷笑]\" ,\"resId\":\"emoji_touxiao\"},\n" +
            "        { \"credentialName\" : \"[再见]\" ,\"resId\":\"emoji_zaijian\"},\n" +
            "        { \"credentialName\" : \"[敲打]\" ,\"resId\":\"emoji_qiaoda\"},\n" +
            "        { \"credentialName\" : \"[擦汗]\" ,\"resId\":\"emoji_cahan\"},\n" +
            "        { \"credentialName\" : \"[猪头]\" ,\"resId\":\"emoji_zhutou\"},\n" +
            "        { \"credentialName\" : \"[玫瑰]\" ,\"resId\":\"emoji_meigui\"},\n" +
            "        { \"credentialName\" : \"[流泪]\" ,\"resId\":\"emoji_liulei\"},\n" +
            "        { \"credentialName\" : \"[大哭]\" ,\"resId\":\"emoji_daku\"},\n" +
            "        { \"credentialName\" : \"[嘘]\" ,\"resId\":\"emoji_xu\"},\n" +
            "        { \"credentialName\" : \"[酷]\" ,\"resId\":\"emoji_ku\"},\n" +
            "        { \"credentialName\" : \"[抓狂]\" ,\"resId\":\"emoji_zhuakuang\"},\n" +
            "        { \"credentialName\" : \"[委屈]\" ,\"resId\":\"emoji_weiqu\"},\n" +
            "        { \"credentialName\" : \"[便便]\" ,\"resId\":\"emoji_bianbian\"},\n" +
            "        { \"credentialName\" : \"[炸弹]\" ,\"resId\":\"emoji_zhadan\"},\n" +
            "        { \"credentialName\" : \"[菜刀]\" ,\"resId\":\"emoji_caidao\"},\n" +
            "        { \"credentialName\" : \"[可爱]\" ,\"resId\":\"emoji_keai\"},\n" +
            "        { \"credentialName\" : \"[色]\" ,\"resId\":\"emoji_se\"},\n" +
            "        { \"credentialName\" : \"[害羞]\" ,\"resId\":\"emoji_haixiu\"},\n" +
            "        { \"credentialName\" : \"[得意]\" ,\"resId\":\"emoji_deyi\"},\n" +
            "        { \"credentialName\" : \"[吐]\" ,\"resId\":\"emoji_tu\"},\n" +
            "        { \"credentialName\" : \"[微笑]\" ,\"resId\":\"emoji_weixiao\"},\n" +
            "        { \"credentialName\" : \"[怒]\" ,\"resId\":\"emoji_nu\"},\n" +
            "        { \"credentialName\" : \"[尴尬]\" ,\"resId\":\"emoji_ganga\"},\n" +
            "        { \"credentialName\" : \"[惊恐]\" ,\"resId\":\"emoji_jingkong\"},\n" +
            "        { \"credentialName\" : \"[冷汗]\" ,\"resId\":\"emoji_lenghan\"},\n" +
            "        { \"credentialName\" : \"[爱心]\" ,\"resId\":\"emoji_aixin\"},\n" +
            "        { \"credentialName\" : \"[示爱]\" ,\"resId\":\"emoji_shiai\"},\n" +
            "        { \"credentialName\" : \"[白眼]\" ,\"resId\":\"emoji_baiyan\"},\n" +
            "        { \"credentialName\" : \"[傲慢]\" ,\"resId\":\"emoji_aoman\"},\n" +
            "        { \"credentialName\" : \"[难过]\" ,\"resId\":\"emoji_nanguo\"},\n" +
            "        { \"credentialName\" : \"[惊讶]\" ,\"resId\":\"emoji_jingya\"},\n" +
            "        { \"credentialName\" : \"[疑问]\" ,\"resId\":\"emoji_yiwen\"},\n" +
            "        { \"credentialName\" : \"[困]\" ,\"resId\":\"emoji_kun\"},\n" +
            "        { \"credentialName\" : \"[么么哒]\" ,\"resId\":\"emoji_memeda\"},\n" +
            "        { \"credentialName\" : \"[憨笑]\" ,\"resId\":\"emoji_hanxiao\"},\n" +
            "        { \"credentialName\" : \"[爱情]\" ,\"resId\":\"emoji_aiqing\"},\n" +
            "        { \"credentialName\" : \"[衰]\" ,\"resId\":\"emoji_shuai\"},\n" +
            "        { \"credentialName\" : \"[撇嘴]\" ,\"resId\":\"emoji_biezui\"},\n" +
            "        { \"credentialName\" : \"[阴险]\" ,\"resId\":\"emoji_yinxian\"},\n" +
            "        { \"credentialName\" : \"[奋斗]\" ,\"resId\":\"emoji_fendou\"},\n" +
            "        { \"credentialName\" : \"[发呆]\" ,\"resId\":\"emoji_fadai\"},\n" +
            "        { \"credentialName\" : \"[右哼哼]\" ,\"resId\":\"emoji_youhengheng\"},\n" +
            "        { \"credentialName\" : \"[抱抱]\" ,\"resId\":\"emoji_baobao\"},\n" +
            "        { \"credentialName\" : \"[坏笑]\" ,\"resId\":\"emoji_huaixiao\"},\n" +
            "        { \"credentialName\" : \"[飞吻]\" ,\"resId\":\"emoji_feiwen\"},\n" +
            "        { \"credentialName\" : \"[鄙视]\" ,\"resId\":\"emoji_bishi\"},\n" +
            "        { \"credentialName\" : \"[晕]\" ,\"resId\":\"emoji_yun\"},\n" +
            "        { \"credentialName\" : \"[大兵]\" ,\"resId\":\"emoji_dabing\"},\n" +
            "        { \"credentialName\" : \"[可怜]\" ,\"resId\":\"emoji_kelian\"},\n" +
            "        { \"credentialName\" : \"[强]\" ,\"resId\":\"emoji_qiang\"},\n" +
            "        { \"credentialName\" : \"[弱]\" ,\"resId\":\"emoji_ruo\"},\n" +
            "        { \"credentialName\" : \"[握手]\" ,\"resId\":\"emoji_woshou\"},\n" +
            "        { \"credentialName\" : \"[胜利]\" ,\"resId\":\"emoji_shengli\"},\n" +
            "        { \"credentialName\" : \"[抱拳]\" ,\"resId\":\"emoji_baoquan\"},\n" +
            "        { \"credentialName\" : \"[凋谢]\" ,\"resId\":\"emoji_diaoxie\"},\n" +
            "        { \"credentialName\" : \"[米饭]\" ,\"resId\":\"emoji_mifan\"},\n" +
            "        { \"credentialName\" : \"[蛋糕]\" ,\"resId\":\"emoji_dangao\"},\n" +
            "        { \"credentialName\" : \"[西瓜]\" ,\"resId\":\"emoji_xigua\"},\n" +
            "        { \"credentialName\" : \"[啤酒]\" ,\"resId\":\"emoji_pijiu\"},\n" +
            "        { \"credentialName\" : \"[瓢虫]\" ,\"resId\":\"emoji_piaochong\"},\n" +
            "        { \"credentialName\" : \"[勾引]\" ,\"resId\":\"emoji_gouyin\"},\n" +
            "        { \"credentialName\" : \"[OK]\" ,\"resId\":\"emoji_ok\"},\n" +
            "        { \"credentialName\" : \"[爱你]\" ,\"resId\":\"emoji_aini\"},\n" +
            "        { \"credentialName\" : \"[咖啡]\" ,\"resId\":\"emoji_kafei\"},\n" +
            "        { \"credentialName\" : \"[月亮]\" ,\"resId\":\"emoji_yueliang\"},\n" +
            "        { \"credentialName\" : \"[刀]\" ,\"resId\":\"emoji_dao\"},\n" +
            "        { \"credentialName\" : \"[发抖]\" ,\"resId\":\"emoji_fadou\"},\n" +
            "        { \"credentialName\" : \"[差劲]\" ,\"resId\":\"emoji_chajin\"},\n" +
            "        { \"credentialName\" : \"[拳头]\" ,\"resId\":\"emoji_quantou\"},\n" +
            "        { \"credentialName\" : \"[心碎了]\" ,\"resId\":\"emoji_xinsuile\"},\n" +
            "        { \"credentialName\" : \"[太阳]\" ,\"resId\":\"emoji_taiyang\"},\n" +
            "        { \"credentialName\" : \"[礼物]\" ,\"resId\":\"emoji_liwu\"},\n" +
            "        { \"credentialName\" : \"[皮球]\" ,\"resId\":\"emoji_piqiu\"},\n" +
            "        { \"credentialName\" : \"[骷髅]\" ,\"resId\":\"emoji_kulou\"},\n" +
            "        { \"credentialName\" : \"[挥手]\" ,\"resId\":\"emoji_huishou\"},\n" +
            "        { \"credentialName\" : \"[闪电]\" ,\"resId\":\"emoji_shandian\"},\n" +
            "        { \"credentialName\" : \"[饥饿]\" ,\"resId\":\"emoji_jie\"},\n" +
            "        { \"credentialName\" : \"[困]\" ,\"resId\":\"emoji_kun\"},\n" +
            "        { \"credentialName\" : \"[咒骂]\" ,\"resId\":\"emoji_zhouma\"},\n" +
            "        { \"credentialName\" : \"[折磨]\" ,\"resId\":\"emoji_zhemo\"},\n" +
            "        { \"credentialName\" : \"[抠鼻]\" ,\"resId\":\"emoji_koubi\"},\n" +
            "        { \"credentialName\" : \"[鼓掌]\" ,\"resId\":\"emoji_guzhang\"},\n" +
            "        { \"credentialName\" : \"[糗大了]\" ,\"resId\":\"emoji_qiudale\"},\n" +
            "        { \"credentialName\" : \"[左哼哼]\" ,\"resId\":\"emoji_zuohengheng\"},\n" +
            "        { \"credentialName\" : \"[打哈欠]\" ,\"resId\":\"emoji_dahaqian\"},\n" +
            "        { \"credentialName\" : \"[快哭了]\" ,\"resId\":\"emoji_kuaikule\"},\n" +
            "        { \"credentialName\" : \"[吓]\" ,\"resId\":\"emoji_xia\"},\n" +
            "        { \"credentialName\" : \"[篮球]\" ,\"resId\":\"emoji_lanqiu\"},\n" +
            "        { \"credentialName\" : \"[乒乓]\" ,\"resId\":\"emoji_pingpang\"},\n" +
            "        { \"credentialName\" : \"[NO]\" ,\"resId\":\"emoji_no\"},\n" +
            "        { \"credentialName\" : \"[跳跳]\" ,\"resId\":\"emoji_tiaotiao\"},\n" +
            "        { \"credentialName\" : \"[怄火]\" ,\"resId\":\"emoji_ouhuo\"},\n" +
            "        { \"credentialName\" : \"[转圈]\" ,\"resId\":\"emoji_zhuanquan\"},\n" +
            "        { \"credentialName\" : \"[磕头]\" ,\"resId\":\"emoji_ketou\"},\n" +
            "        { \"credentialName\" : \"[回头]\" ,\"resId\":\"emoji_huitou\"},\n" +
            "        { \"credentialName\" : \"[跳绳]\" ,\"resId\":\"emoji_tiaosheng\"},\n" +
            "        { \"credentialName\" : \"[激动]\" ,\"resId\":\"emoji_jidong\"},\n" +
            "        { \"credentialName\" : \"[街舞]\" ,\"resId\":\"emoji_jiewu\"},\n" +
            "        { \"credentialName\" : \"[献吻]\" ,\"resId\":\"emoji_xianwen\"},\n" +
            "        { \"credentialName\" : \"[左太极]\" ,\"resId\":\"emoji_zuotaiji\"},\n" +
            "        { \"credentialName\" : \"[右太极]\" ,\"resId\":\"emoji_youtaiji\"},\n" +
            "        { \"credentialName\" : \"[闭嘴]\" ,\"resId\":\"emoji_bizui\"},\n" +
            "        { \"credentialName\" : \"[猫咪]\" ,\"resId\":\"emoji_maomi\"},\n" +
            "        { \"credentialName\" : \"[红双喜]\" ,\"resId\":\"emoji_hongshuangxi\"},\n" +
            "        { \"credentialName\" : \"[鞭炮]\" ,\"resId\":\"emoji_bianpao\"},\n" +
            "        { \"credentialName\" : \"[红灯笼]\" ,\"resId\":\"emoji_hongdenglong\"},\n" +
            "        { \"credentialName\" : \"[麻将]\" ,\"resId\":\"emoji_majiang\"},\n" +
            "        { \"credentialName\" : \"[麦克风]\" ,\"resId\":\"emoji_maikefeng\"},\n" +
            "        { \"credentialName\" : \"[礼品袋]\" ,\"resId\":\"emoji_lipindai\"},\n" +
            "        { \"credentialName\" : \"[信封]\" ,\"resId\":\"emoji_xinfeng\"},\n" +
            "        { \"credentialName\" : \"[象棋]\" ,\"resId\":\"emoji_xiangqi\"},\n" +
            "        { \"credentialName\" : \"[彩带]\" ,\"resId\":\"emoji_caidai\"},\n" +
            "        { \"credentialName\" : \"[蜡烛]\" ,\"resId\":\"emoji_lazhu\"},\n" +
            "        { \"credentialName\" : \"[爆筋]\" ,\"resId\":\"emoji_baojin\"},\n" +
            "        { \"credentialName\" : \"[棒棒糖]\" ,\"resId\":\"emoji_bangbangtang\"},\n" +
            "        { \"credentialName\" : \"[奶瓶]\",\"resId\":\"emoji_neiping\" },\n" +
            "        { \"credentialName\" : \"[面条]\" ,\"resId\":\"emoji_miantiao\"},\n" +
            "        { \"credentialName\" : \"[香蕉]\" ,\"resId\":\"emoji_xiangjiao\"},\n" +
            "        { \"credentialName\" : \"[飞机]\" ,\"resId\":\"emoji_feiji\"},\n" +
            "        { \"credentialName\" : \"[左车头]\" ,\"resId\":\"emoji_zuochetou\"},\n" +
            "        { \"credentialName\" : \"[车厢]\" ,\"resId\":\"emoji_chexiang\"},\n" +
            "        { \"credentialName\" : \"[右车头]\" ,\"resId\":\"emoji_youchetou\"},\n" +
            "        { \"credentialName\" : \"[多云]\" ,\"resId\":\"emoji_duoyun\"},\n" +
            "        { \"credentialName\" : \"[下雨]\" ,\"resId\":\"emoji_xiayu\"},\n" +
            "        { \"credentialName\" : \"[钞票]\" ,\"resId\":\"emoji_chaopiao\"},\n" +
            "        { \"credentialName\" : \"[熊猫]\" ,\"resId\":\"emoji_xiongmao\"},\n" +
            "        { \"credentialName\" : \"[灯泡]\" ,\"resId\":\"emoji_dengpao\"},\n" +
            "        { \"credentialName\" : \"[风车]\" ,\"resId\":\"emoji_fengche\"},\n" +
            "        { \"credentialName\" : \"[闹钟]\" ,\"resId\":\"emoji_naozhong\"},\n" +
            "        { \"credentialName\" : \"[彩球]\" ,\"resId\":\"emoji_caiqiu\"},\n" +
            "        { \"credentialName\" : \"[钻戒]\" ,\"resId\":\"emoji_zuanjie\"},\n" +
            "        { \"credentialName\" : \"[沙发]\" ,\"resId\":\"emoji_shafa\"},\n" +
            "        { \"credentialName\" : \"[纸巾]\" ,\"resId\":\"emoji_zhijin\"},\n" +
            "        { \"credentialName\" : \"[手枪]\" ,\"resId\":\"emoji_shouqiang\"},\n" +
            "        { \"credentialName\" : \"[青蛙]\" ,\"resId\":\"emoji_qingwa\"}\n" +
            " ]}";

    public void init() {
        if (init) {
            return;
        }
        TaskExecutor.getInstance().createAsycTask(() -> {
            EmojiJson emojiJson = new Gson().fromJson(EmojiUtils.emojiJson, EmojiJson.class);
            return emojiJson;
        }, result -> {
            EmojiJson emojiJson = (EmojiJson) result;
            emojiMap = new HashMap<>();
            emojiBeans = ((EmojiJson) result).getEmojis();
            for (EmojiJson.EmojiBean emojiBean : emojiBeans) {
                emojiMap.put(emojiBean.getCredentialName(), emojiBean.getResId());
            }
            init = true;
        });
    }

    public Map<String, String> getEmojiMap() {
        return emojiMap;
    }

    public List<EmojiJson.EmojiBean> getEmojiBeans() {
        return emojiBeans;
    }


    public static SpannableString generateEmojiSpan(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        long time = System.currentTimeMillis();
        SpannableString contentShow = new SpannableString(content);
        Pattern pattern = Pattern.compile(EmojiUtils.EMOJI_MATCH);
        Matcher matcher = pattern.matcher(content);
        Map<String, String> emojiMap = EmojiUtils.getInstance().getEmojiMap();
        int start;
        int end;
        String resId;
        ImageSpan imageSpan;
        while (matcher.find()) {
            start = matcher.start();
            end = matcher.end();
            resId = emojiMap.get(matcher.group());
            if (resId != null) {
                imageSpan = new ImageSpan(ImBaseBridge.getInstance().getApplication(), Utils.getDrawableByName(resId));
                contentShow.setSpan(imageSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        LogUtils.log(TAG, String.format("generateEmojiSpan time:%d", System.currentTimeMillis() - time));
        return contentShow;
    }
}

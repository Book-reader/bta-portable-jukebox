package bookreader.portable_jukebox;

import java.lang.reflect.Field;
import java.util.concurrent.locks.Lock;

import bookreader.portable_jukebox.gui.screen.ScreenPortableJukebox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.sound.SoundCategoryHelper;
import net.minecraft.client.sound.SoundEngine;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.client.sound.SoundRepository;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.sound.SoundCategory;
import paulscode.sound.SoundSystem;

@Environment(EnvType.CLIENT)
public class SoundUtils {
    public static String SOUND_CATEGORY = SoundEngine.BG_MUSIC; //"PortableMusic";

    public static void playRecordAt(ItemDiscMusic record, Player player)
    {
        try
        {
            Field mc_field = PlayerLocal.class.getDeclaredField("mc");
            mc_field.setAccessible(true);
            Minecraft mc = (Minecraft)mc_field.get((PlayerLocal)player);
            Field options = SoundEngine.class.getDeclaredField("options");
            options.setAccessible(true);

            Field lock = SoundEngine.class.getDeclaredField("lock");
            lock.setAccessible(true);

            SoundSystem soundSystem = SoundEngine.getSoundSystem();
            SoundEntry record_sound = SoundRepository.SOUNDS.getSoundEntry(record.recordName);
            try
            {
                ((Lock)lock.get(null)).lock();
                if (soundSystem.playing(SoundUtils.SOUND_CATEGORY)) soundSystem.stop(SoundUtils.SOUND_CATEGORY);
                soundSystem.backgroundMusic(SoundUtils.SOUND_CATEGORY, record_sound.getURL(), record_sound.name, false);
                soundSystem.setPitch(SoundUtils.SOUND_CATEGORY, record_sound.pitch);
                soundSystem.setVolume(SoundUtils.SOUND_CATEGORY, SoundCategoryHelper.getEffectiveVolume(SoundCategory.MUSIC, (GameSettings)options.get(mc.sndManager)) * record_sound.volume);
                soundSystem.play(SoundUtils.SOUND_CATEGORY);
            } finally
            {
                ((Lock)lock.get(null)).unlock();
            }

            if (record.recordAuthor != null)
            {
                mc.hudIngame.setRecordPlayingMessage(record.recordAuthor + " - " + I18n.getInstance().translateKey(record.recordName));
            }
            else
            {
                mc.hudIngame.setRecordPlayingMessage(I18n.getInstance().translateKey(record.getKey()));
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}

package bookreader.portable_jukebox;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;

import bookreader.portable_jukebox.gui.screen.ScreenPortableJukebox;
import bookreader.portable_jukebox.util.Util;
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
import org.jetbrains.annotations.NotNull;
import paulscode.sound.SoundSystem;

@Environment(EnvType.CLIENT)
public class SoundUtils {
    public static final String SOUND_CATEGORY = "PortableMusic";
    private static boolean started = false;
    private static boolean paused = false;
    private static ItemDiscMusic current_record;
    private static final Minecraft mc = Util.getMinecraft();
    private static final Lock LOCK = getLock();

    @SuppressWarnings("DataFlowIssue")
	private static final @NotNull SoundSystem snd = SoundEngine.getSoundSystem();

    public static boolean playing()
    {
        return inLock(() -> snd.playing(SOUND_CATEGORY));
    }

    public static void pause()
    {
        inLock(() -> {
            paused = true;
            snd.pause(SOUND_CATEGORY);
            return null;
        });
    }

    public static void unpause()
    {
        inLock(() -> {
            paused = false;
            snd.play(SOUND_CATEGORY);
            return null;
        });
    }

    public static void stop()
    {
        inLock(() -> {
            paused = false;
            started = false;
            snd.stop(SOUND_CATEGORY);
            return null;
        });
    }

    public static boolean started()
    {
        if (started && !playing() && !paused)
        {
            PortableJukebox.LOGGER.info("setting started to false");
            started = false;
        }
        return started;
    }

    public static boolean started_noupdate()
    {
        return started;
    }

    public static ItemDiscMusic currentRecord()
    {
        return current_record;
    }

    public static void playRecordAt(ItemDiscMusic record, Player player)
    {
        try
        {
            SoundEntry record_sound = SoundRepository.SOUNDS.getSoundEntry(record.recordName);
            try
            {
                LOCK.lock();
                started = true;
                paused = false;
                current_record = record;
                if (snd.playing(SOUND_CATEGORY)) snd.stop(SOUND_CATEGORY);
                snd.backgroundMusic(SOUND_CATEGORY, record_sound.getURL(), record_sound.name, false);
                snd.setPitch(SOUND_CATEGORY, record_sound.pitch);
                snd.setVolume(SOUND_CATEGORY, SoundCategoryHelper.getEffectiveVolume(SoundCategory.MUSIC, mc.gameSettings) * record_sound.volume);
                snd.play(SOUND_CATEGORY);
            } finally
            {
                LOCK.unlock();
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

    private static Lock getLock()
    {
        try
        {
            Field lock = SoundEngine.class.getDeclaredField("lock");
            lock.setAccessible(true);
            return (Lock)lock.get(null);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private static <T> T inLock(Callable<T> callable)
    {
        try
        {
            LOCK.lock();
            return callable.call();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            LOCK.unlock();
        }
    }
}

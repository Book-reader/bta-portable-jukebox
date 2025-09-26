package bookreader.portable_jukebox.mixin;

import java.nio.FloatBuffer;

import org.lwjgl.openal.AL10;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import bookreader.portable_jukebox.SoundUtils;
import bookreader.portable_jukebox.item.PortableJukeboxItem;
import net.betterthanadventure.sound.ChannelLWJGL3OpenAL;
import net.betterthanadventure.sound.SourceLWJGL3OpenAL;
import net.minecraft.client.sound.SoundEngine;
import paulscode.sound.FilenameURL;
import paulscode.sound.SoundBuffer;
import paulscode.sound.Source;

@Mixin(value = SourceLWJGL3OpenAL.class, remap = false)
public class DisableDirectionWithAttModelZero extends Source
{
    public DisableDirectionWithAttModelZero(boolean arg0, boolean arg1, boolean arg2, String arg3, FilenameURL arg4,
            SoundBuffer arg5, float arg6, float arg7, float arg8, int arg9, float arg10, boolean arg11) {
        super(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
    }

    @Shadow
    private FloatBuffer listenerPosition;
    @Shadow
    private FloatBuffer sourcePosition;
    @Shadow
    private ChannelLWJGL3OpenAL channelOpenAL;
    @Shadow
    private boolean checkALError() {throw new AssertionError();}

    @Inject(method = "positionChanged()V", at = @At("HEAD"))
    public void positionChanged(CallbackInfo info)
    {
        if (this.attModel == 0 && this.sourcename == SoundUtils.SOUND_CATEGORY && !this.listenerPosition.equals(this.sourcePosition))
        {
            this.distanceFromListener = 0.0f;
            if (this.channel != null && this.channel.attachedSource == this && this.channelOpenAL != null && this.channelOpenAL.ALSource != null)
            {
                this.sourcePosition.put(0, this.listenerPosition.get(0));
                this.sourcePosition.put(1, this.listenerPosition.get(1));
                this.sourcePosition.put(2, this.listenerPosition.get(2));
                AL10.alSourcefv(this.channelOpenAL.ALSource.get(0), AL10.AL_POSITION, this.sourcePosition);
                this.checkALError();
            }
        }   
    }
}

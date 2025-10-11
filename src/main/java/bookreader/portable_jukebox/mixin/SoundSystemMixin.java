package bookreader.portable_jukebox.mixin;

import bookreader.portable_jukebox.SoundUtils;
import bookreader.portable_jukebox.iface.PlayDiscFromPlayer;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import paulscode.sound.CommandObject;
import paulscode.sound.CommandThread;
import paulscode.sound.SoundSystem;

//@Mixin(value = SoundSystem.class, remap = false)
//public class SoundSystemMixin implements PlayDiscFromPlayer
//{
//	@Shadow
//	public boolean CommandQueue(CommandObject cmd) {throw new AssertionError();}
//	@Shadow
//	protected CommandThread commandThread;
//
//	@Unique
//	public void bta_portable_jukebox$setEmitter(String sound_category, Player emitter)
//	{
//		this.CommandQueue(new CommandObject(SoundUtils.SOUND_COMMAND, sound_category, emitter));
//		this.commandThread.interrupt();
//	}
//
//	@WrapOperation(method = "CommandQueue(Lpaulscode/sound/CommandObject;)Z", at = @At(value = "FIELD", target = "Lpaulscode/sound/CommandObject;Command:I"))
//	private int onAccessCommandQueueCommandType(CommandObject instance, Operation<Integer> original)
//	{
//		int val = original.call(instance);
//		if (val == SoundUtils.SOUND_COMMAND)
//		{
//
//		}
//		return val;
//	}
//}

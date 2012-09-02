package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientToggleDownfall extends CommandToggleDownfall
{
    public CommandClientToggleDownfall()
    {
    }

    protected void func_71554_c()
    {
        Minecraft.getMinecraft().theWorld.getWorldInfo().setRainTime(1);
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }
}
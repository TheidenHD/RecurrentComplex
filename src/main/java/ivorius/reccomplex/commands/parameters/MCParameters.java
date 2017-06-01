/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://ivorius.net
 */

package ivorius.reccomplex.commands.parameters;

import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

/**
 * Created by lukas on 31.05.17.
 */
public class MCParameters extends Parameters
{
    public MCParameters(Parameters blueprint)
    {
        super(blueprint);
    }

    public static MCParameters of(String[] args, String... flags)
    {
        return of(args, flags, MCParameters::new);
    }

    public MCParameter mc()
    {
        return new MCParameter(get());
    }

    public MCParameter mc(@Nonnull String name)
    {
        return new MCParameter(get(name));
    }

    public Parameter.Result<BlockPos> pos(String x, String y, String z, BlockPos ref, boolean centerBlock)
    {
        return this.mc(x).pos(this.get(y), this.get(z), ref, centerBlock);
    }
}
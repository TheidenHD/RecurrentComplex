/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.reccomplex.commands;

import ivorius.ivtoolkit.blocks.BlockCoord;
import ivorius.ivtoolkit.math.AxisAlignedTransform2D;
import ivorius.reccomplex.RCConfig;
import ivorius.reccomplex.operation.OperationRegistry;
import ivorius.reccomplex.structures.OperationGenerateStructure;
import ivorius.reccomplex.structures.StructureRegistry;
import ivorius.reccomplex.structures.StructureInfo;
import ivorius.reccomplex.worldgen.WorldGenStructures;
import ivorius.reccomplex.structures.generic.GenericStructureInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by lukas on 25.05.14.
 */
public class CommandGenerateStructure extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return RCConfig.commandPrefix + "gen";
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "commands.strucGen.usage";
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args)
    {
        int x, z;

        if (args.length <= 0)
            throw new WrongUsageException("commands.strucGen.usage");

        String structureName = args[0];
        StructureInfo structureInfo = StructureRegistry.getStructure(structureName);
        World world = commandSender.getEntityWorld();

        if (structureInfo == null)
        {
            throw new CommandException("commands.strucGen.noStructure", structureName);
        }

        x = commandSender.getPlayerCoordinates().posX;
        z = commandSender.getPlayerCoordinates().posZ;

        if (args.length >= 3)
        {
            x = MathHelper.floor_double(func_110666_a(commandSender, (double) x, args[1]));
            z = MathHelper.floor_double(func_110666_a(commandSender, (double) z, args[2]));
        }

        if (structureInfo instanceof GenericStructureInfo)
        {
            Random random = world.rand;

            AxisAlignedTransform2D transform = AxisAlignedTransform2D.transform(structureInfo.isRotatable() ? random.nextInt(4) : 0, structureInfo.isMirrorable() && random.nextBoolean());

            int[] size = WorldGenStructures.structureSize(structureInfo, transform);

            int genX = x - size[0] / 2;
            int genZ = z - size[2] / 2;
            int genY = structureInfo.generationY(world, random, x, z);
            BlockCoord coord = new BlockCoord(genX, genY, genZ);

            OperationRegistry.queueOperation(new OperationGenerateStructure((GenericStructureInfo) structureInfo, transform, coord, false), commandSender);
        }
        else
            WorldGenStructures.generateStructureRandomly(world, world.rand, structureInfo, x, z, false);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender commandSender, String[] args)
    {
        if (args.length == 1)
        {
            Set<String> allStructureNames = StructureRegistry.getAllStructureNames();

            return getListOfStringsMatchingLastWord(args, allStructureNames.toArray(new String[allStructureNames.size()]));
        }
        else if (args.length == 2 || args.length == 3)
        {
            return getListOfStringsMatchingLastWord(args, "~");
        }

        return null;
    }
}

package fr.niixoz.survivalcore.utils;

import fr.niixoz.survivalcore.SurvivalCore;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

/**
 * Écrit un bloc directement dans la chunk, sans enregistrer la position dans la
 * liste des changements en attente du ChunkHolder.
 *
 * Conséquence : aucun paquet de block change n'est envoyé aux clients. Le
 * serveur, lui, voit bien le nouveau bloc (indispensable pour que
 * Player#checkBedExists ne réveille pas le dormeur au tick suivant).
 *
 * Attention : contourne aussi les updates de lumière, la heightmap et
 * Block#onPlace. À réserver à des blocs temporaires et inoffensifs.
 *
 * Le pont CraftBukkit passe par la réflexion pour ne pas dépendre du package
 * versionné (org.bukkit.craftbukkit.vXX_RY). Les types net.minecraft, eux, sont
 * importés directement : il faut donc compiler contre le jar serveur remappé
 * Mojang. Si quoi que ce soit échoue, la méthode renvoie false une bonne fois
 * pour toutes et l'appelant retombe sur l'API classique.
 */
public final class SilentBlockUtil {

    private static boolean available = true;

    private SilentBlockUtil() {}

    public static boolean setSilently(Block block, BlockData data) {
        if(!available)
            return false;

        try {
            World world = block.getWorld();
            ServerLevel level = (ServerLevel) world.getClass().getMethod("getHandle").invoke(world);

            net.minecraft.world.level.block.state.BlockState state =
                    (net.minecraft.world.level.block.state.BlockState)
                            data.getClass().getMethod("getState").invoke(data);

            BlockPos pos = new BlockPos(block.getX(), block.getY(), block.getZ());
            LevelChunk chunk = level.getChunkAt(pos);
            chunk.setBlockState(pos, state, 0);
            return true;
        }
        catch(Throwable throwable) {
            available = false;
            SurvivalCore.getInstance().getLogger().warning(
                    "[SilentBlockUtil] Écriture silencieuse indisponible (" + throwable + "), "
                            + "repli sur l'API Bukkit : un bref clignotement du lit est attendu.");
            return false;
        }
    }
}
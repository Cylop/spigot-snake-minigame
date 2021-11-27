package at.malibu.general.testing;

import at.malibu.api.commands.CommandExecutorType;
import at.malibu.api.commands.annotations.BaseCommandInfo;
import at.malibu.api.commands.annotations.HasPermission;
import at.malibu.api.commands.annotations.OnlyFor;
import at.malibu.general.PaperTesting;
import at.malibu.general.commands.AbstractBaseCommand;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

@HasPermission(permission = "my.permission")
@OnlyFor(executor = CommandExecutorType.PLAYER)
@BaseCommandInfo(name = "test", description = "Das ist meine Beschreibung", usageMessage = "Nutze /test liuliuliu")
public class TestCommand extends AbstractBaseCommand {

    @Override
    public void execute(Player player, String[] args) {

        List<Entity> entities = new ArrayList<>();

        int amount = 10;
        double increment = (2 * Math.PI) / amount;

        Location center = player.getLocation();

        final int radius = 4;
        final int precision = amount;

        for (int i = 0; i < amount; i++) {
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));

            var entity = player.getWorld().spawnEntity(new Location(center.getWorld(), x, center.getY() - 5, z), EntityType.THROWN_EXP_BOTTLE); //.spawnFallingBlock(player.getLocation(), Material.ANVIL.createBlockData());

            entity.setGravity(false);
            entity.setInvulnerable(true);

            entities.add(entity);
        }


        new BukkitRunnable() {
            int current = 0;

            @Override
            public void run() {

                for (int i = 0; i < entities.size(); i++) {
                    var entity = entities.get(i);
                    double p1 = (current) * Math.PI / (precision / 2) + (i * increment);
                    double p2 = (current) * Math.PI / (precision / 2);

                    double x1 = Math.cos(p1) * radius;
                    double x2 = Math.cos(p2) * radius;
                    double z1 = Math.sin(p1) * radius;
                    double z2 = Math.sin(p2) * radius;

                    Vector vec = new Vector(x2 - x1, Math.sin(current / 4) / 10, z2 - z1);
                    entity.setVelocity(vec);
                }

                current++;
                if (current > precision)
                    current = 0;

            }
        }.runTaskTimer(PaperTesting.getInstance(), 0L, 1L);
    }
}

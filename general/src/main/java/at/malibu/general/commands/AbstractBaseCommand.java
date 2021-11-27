package at.malibu.general.commands;

import at.malibu.api.commands.CommandExecution;
import at.malibu.api.commands.CommandExecutorType;
import at.malibu.api.commands.annotations.BaseCommandInfo;
import at.malibu.api.commands.annotations.HasPermission;
import at.malibu.api.commands.annotations.OnlyFor;
import at.malibu.api.commands.annotations.ValidateArgs;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractBaseCommand implements CommandExecution {

    @Getter
    private final Command command;

    private final BaseCommandInfo baseCommandInfoAnnotation;

    private final OnlyFor onlyForAnnotation;
    private final HasPermission hasPermissionAnnotation;

    private final ValidateArgs validateArgsAnnotation;

    public AbstractBaseCommand() {

        this.baseCommandInfoAnnotation = this.getClass().getDeclaredAnnotation(BaseCommandInfo.class);
        Preconditions.checkNotNull(this.baseCommandInfoAnnotation, "BaseCommandInfo must not be null. Is the Annotation present?");
        Preconditions.checkNotNull(this.baseCommandInfoAnnotation.name(), "Name must not be null. Its required for the registration");

        this.onlyForAnnotation = this.getClass().getDeclaredAnnotation(OnlyFor.class);
        this.hasPermissionAnnotation = this.getClass().getDeclaredAnnotation(HasPermission.class);
        this.validateArgsAnnotation = this.getClass().getDeclaredAnnotation(ValidateArgs.class);

        this.command = new Command(this.baseCommandInfoAnnotation.name()) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                return AbstractBaseCommand.this.onCommand(sender, commandLabel, args);
            }

            @Override
            public @NotNull
            List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                return AbstractBaseCommand.this.tabComplete(sender, alias, args);
            }
        };

        if (!this.baseCommandInfoAnnotation.description().isBlank())
            this.command.setDescription(this.baseCommandInfoAnnotation.description());
        if (this.baseCommandInfoAnnotation.labels().length > 0)
            this.command.setAliases(Arrays.asList(this.baseCommandInfoAnnotation.labels()));
        if (!this.baseCommandInfoAnnotation.usageMessage().isBlank())
            this.command.setUsage(this.baseCommandInfoAnnotation.usageMessage());


        this.command.setPermission(this.hasPermissionAnnotation.permission());
    }

    public List<String> tabComplete(@NonNull CommandSender sender, @NonNull String alias, @NonNull String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 0 || !sender.getServer().suggestPlayerNamesWhenNullTabCompletions()) { // Paper - allow preventing player name suggestions by default) {
            return ImmutableList.of();
        }

        String lastWord = args[args.length - 1];

        Player senderPlayer = sender instanceof Player ? (Player) sender : null;

        List<String> matchedPlayers = new ArrayList<>();
        for (Player player : sender.getServer().getOnlinePlayers()) {
            String name = player.getName();
            if ((senderPlayer == null || senderPlayer.canSee(player)) && StringUtil.startsWithIgnoreCase(name, lastWord)) {
                matchedPlayers.add(name);
            }
        }

        matchedPlayers.sort(String.CASE_INSENSITIVE_ORDER);
        return matchedPlayers;
    }

    public boolean onCommand(@NonNull CommandSender sender, @NonNull String commandLabel, String[] args) {
        this.execute(sender, args);
        return true;
    }

    public final void execute(CommandSender sender, String[] args) {
        if (!this.validateExecutorType(sender)) {
            // todo: send correct message
            sender.sendMessage(String.format("message that this command can only be executed by: %s", Arrays.stream(this.onlyForAnnotation.executor()).map(CommandExecutorType::getHumanName).collect(Collectors.joining(", "))));
            return;
        }

        if (!this.validatePermission(sender)) {
            // todo: send correct message
            sender.sendMessage(String.format("message that the player does not have the permission: %s", this.hasPermissionAnnotation.permission()));
            return;
        }

        if (!this.validateArgs(args)) {
            sender.sendMessage(String.format("message that the args are to long/short. Args length: %s", args.length));
            return;
        }

        if (sender instanceof ConsoleCommandSender) {
            this.execute((ConsoleCommandSender) sender, args);
        } else if (sender instanceof Player) {
            this.execute((Player) sender, args);
        }
    }

    private boolean validateExecutorType(CommandSender sender) {
        if (this.onlyForAnnotation == null || this.onlyForAnnotation.executor() == null) return true;

        return Arrays.stream(this.onlyForAnnotation.executor()).filter((exec) -> exec.isFor(sender)).count() > 0;
    }

    private boolean validatePermission(CommandSender sender) {
        if (this.hasPermissionAnnotation == null || this.hasPermissionAnnotation.permission() == null || this.hasPermissionAnnotation.permission().isBlank())
            return true;
        return sender.hasPermission(this.hasPermissionAnnotation.permission());
    }

    private boolean validateArgs(@NonNull String[] args) {
        if (this.validateArgsAnnotation == null) return true;
        return this.validateArgsAnnotation.minArgs() <= args.length && this.validateArgsAnnotation.maxArgs() >= args.length;
    }

    @Override
    public void execute(ConsoleCommandSender sender, String[] args) {
    }

    @Override
    public void execute(Player player, String[] args) {
    }

    public String getName() {
        return this.baseCommandInfoAnnotation.name();
    }
}

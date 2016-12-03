package kuroodo.discordbot.chatcommands;

import java.util.Random;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

public class CommandRoulette extends ChatCommand {

	private final int SHOTS = 4;
	private final float DEATHCOOLDOWN = 15;
	private final String DEATHROLENAME = "RIP"; // Name of role to give to dead
												// users
	private float currentCoolTime = 0;
	private boolean dead = false, isRevolverRaised = false, isPulledTrigger = false;

	public CommandRoulette() {
		super();
		shouldUpdate = true;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		currentCoolTime += delta;
		updateRoulette();
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		updateRoulette();
	}

	private void updateRoulette() {
		if (!dead) {
			// raise revolver to head
			if (currentCoolTime >= 1 && currentCoolTime <= 2 && !isRevolverRaised) {
				sendMessage(event.getMember().getAsMention() + " raises a revolver to their head");
				isRevolverRaised = true;
				// pull trigger
			} else if (currentCoolTime >= 4 && currentCoolTime <= 6 && !isPulledTrigger) {
				playRoulette();
			}
		} else if (currentCoolTime >= DEATHCOOLDOWN) {
			resetRoles();
			shouldUpdate = false;
		}
	}

	private void playRoulette() {
		Random rand = new Random(System.nanoTime());
		int randomNumber = rand.nextInt(SHOTS);

		if (randomNumber == 1) {
			dead = true;
			currentCoolTime = 0;
			sendMessage(event.getAuthor().getAsMention()
					+ " pulls the trigger and BAM! Their brain splatters all over the place!");
			changeUserRoles();
		} else {
			sendMessage(event.getAuthor().getAsMention() + " pulls the trigger and survives to tell the tale!");
			shouldUpdate = false;
		}
		isPulledTrigger = true;
	}

	private void changeUserRoles() {
		GuildController manager = JDAHelper.getGuild().getController();
		manager.addRolesToMember(event.getMember(), JDAHelper.getRoleByName(DEATHROLENAME)).queue();
	}

	private void resetRoles() {
		GuildController manager = JDAHelper.getGuild().getController();
		manager.removeRolesFromMember(event.getMember(), JDAHelper.getRoleByName(DEATHROLENAME)).queue();
	}

	@Override
	public String info() {
		return "Play a game of Russian Roulette. Usage !roulette";
	}
}
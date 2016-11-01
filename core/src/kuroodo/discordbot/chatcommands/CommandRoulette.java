package kuroodo.discordbot.chatcommands;

import java.util.ArrayList;
import java.util.Random;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.ChatHelper;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.managers.GuildManager;

public class CommandRoulette extends ChatCommand {

	private final int SHOTS = 4;
	private final float DEATHCOOLDOWN = 15;
	private final String DEATHROLENAME = "RIP"; // Name of role to give to dead
												// users
	private float currentCoolTime = 0;
	private boolean dead = false, phase1 = false, phase2 = false;

	private ArrayList<Role> usersRoles;

	// private Timer timer;

	// private TimerTask task;

	public CommandRoulette() {
		super();
		usersRoles = new ArrayList<>();
		shouldUpdate = true;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		currentCoolTime += delta;
		checkRoulette();
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		checkRoulette();
	}

	private void checkRoulette() {
		if (!dead) {
			if (currentCoolTime >= 1 && currentCoolTime <= 2 && !phase1) {
				sendMessage(event.getAuthor().getAsMention() + " raises a revolver to their head");
				phase1 = true;
			} else if (currentCoolTime >= 4 && currentCoolTime <= 6 && !phase2) {
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
				phase2 = true;

			}
		} else if (currentCoolTime >= DEATHCOOLDOWN) {
			resetRoles();
			shouldUpdate = false;
		}
	}

	public void changeUserRoles() {
		GuildManager manager = ChatHelper.getGuild().getManager();

		if (ChatHelper.isUserAdmin(event.getAuthor()) || ChatHelper.isUserModerator(event.getAuthor())) {
			manager.addRoleToUser(event.getAuthor(), ChatHelper.getRoleByName(DEATHROLENAME)).update();
			System.out.println("isadmin");
		} else {
			System.out.println("NOT ADMIN");
			for (Role role : ChatHelper.getGuild().getRolesForUser(event.getAuthor())) {
				usersRoles.add(role);
			}

			ChatHelper.removeUsersRoles(event.getAuthor(), manager);
			manager.addRoleToUser(event.getAuthor(), ChatHelper.getRoleByName(DEATHROLENAME));

			manager.update();
		}
	}

	public void resetRoles() {
		GuildManager manager = ChatHelper.getGuild().getManager();
		if (ChatHelper.isUserAdmin(event.getAuthor()) || ChatHelper.isUserModerator(event.getAuthor())) {
			manager.removeRoleFromUser(event.getAuthor(), ChatHelper.getRoleByName(DEATHROLENAME)).update();
		} else {
			ChatHelper.removeUsersRoles(event.getAuthor(), manager);
			for (Role role : usersRoles) {
				manager.addRoleToUser(event.getAuthor(), role);
			}
			manager.update();

		}
	}

	@Override
	public String info() {
		return "Play a game of Russian Roulette. Usage !roulette";
	}

}

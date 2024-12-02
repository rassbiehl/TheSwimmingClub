package swimclub.controllers;

import swimclub.models.Member;
import swimclub.models.Team;
import swimclub.services.TeamService;

import java.util.List;

/**
 * Controller class for managing teams.
 */
public class TeamController {
    private final TeamService teamService;

    /**
     * Constructor to initialize the controller.
     *
     * @param teamService The service for managing teams.
     */
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Creates a new team.
     *
     * @param teamName The name of the team.
     * @return The created Team object.
     */
    public Team createTeam(String teamName, String teamTypeString) {
        return teamService.createTeam(teamName, teamTypeString);
    }

    /**
     * Adds a member to a team.
     *
     * @param teamName The name of the team.
     * @param member   The member to add.
     */
    public void addMemberToTeam(String teamName, Member member) {
        teamService.addMemberToTeam(teamName, member);
    }

    /**
     * Removes a member from a team.
     *
     * @param teamName The name of the team.
     * @param member The member to remove from the team.
     */
    public void removeMemberFromTeam(String teamName, Member member) {
        teamService.removeMemberFromTeam(teamName, member); // Delegate to service to handle the removal logic
    }

    /**
     * Deletes a team by its name.
     *
     * @param teamName The name of the team to delete.
     */
    public void deleteTeam(String teamName) {
        teamService.deleteTeam(teamName); // Delegate to service to handle the deletion logic
    }

    /**
     * Assigns a team leader to a team.
     *
     * @param teamName The name of the team.
     * @param member   The member to assign as the team leader.
     */
    public void assignTeamLeader(String teamName, Member member) {
        teamService.assignTeamLeader(teamName, member); // Delegate to service to handle team leader assignment
    }


    /**
     * Retrieves all teams.
     *
     * @return A list of all teams.
     */
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }
}

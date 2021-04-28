package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.Resources.ResourceType;

import java.util.List;

public class Command {
    String cmd; //this is the command, [set_players, login, initialise_leaderCards , initialise_resources, shift_resources, choose_marbles, choose_leaderCard, insert_in_warehouse, buy_card, select position, production, basic_production, normal_production, leader_production, end_production, leader_action, leader_action_activate, leader_action_discard]
    // set_players
    int numberOfPlayers; // should be from 1 to 4
    //login
    String nickname; // nickname of the player
    // initialize_leaderCards
    int firstCard; // should be from 1 to 4
    int secondCard; // should be from 1 to 4
    // initialise_resources / basic_production
    Resource firstResource;
    Resource secondResource;
    // shift_resources
    int source; //should be from 1 t 5
    int destination; // should be from 1 to 5
    // choose_marbles
    String dimension; // should be "row" or "column"
    // choose_marbles / choose_leaderCard
    int index; // should be from 1 to 3 for rows, 1 to 4 for columns / should be from 1 to 2 for leaderCards
    // insert_in_warehouse
    int shelf; // should be from 1 to 5
    // choose_card
    CardColor color; // should be "green" or "blue" or "purple" or "yellow"
    int level; // should be from 1 to 3
    // select_position
    int dashboardPosition; // should be from 1 to 3
    // select_resources_from_warehouse / normal_production
    CollectionResources toPayFromWarehouse;
    CollectionResources toPayFromStrongbox;
    // basic_production
    boolean toPayFirstFromWarehouse;
    boolean toPaySecondFromWarehouse;
    Resource output;
    // normal_production / leader_production
    int position; // should be from 1 to 3 / should be from 1 to 2
    boolean fromWarehouse;
    // leader_action_activate
    int toActivate; // should be from 1 to 2
    // leader_action_discard
    int toDiscard; // should be from 1 to 2

    int marbles;

    int[] shelves;

    int[] indexes;
}

package crazypet;

//Required Imports
import core.game.Observation;
import core.game.StateObservation;
import crazypet.heuristic.GameAgent;
import tools.ElapsedCpuTimer;
import ontology.Types;
import tools.Vector2d;
import tools.pathfinder.PathFinder;
import tools.pathfinder.Node;


//Agent-Dependent Imports
import java.util.ArrayList;


public class AstarAgent extends GameAgent {

    //Path finder (A star algorithm)
    private PathFinder pathFinder;

    //Agent state
    private int step_count;

    //Item state
    private ArrayList<Observation> itemsList[];

    //Portal state
    private ArrayList<Observation> portalsList[];

    //Npc state
    private ArrayList<Observation> npcsList[];

    //Constructor. It must return in 1 second maximum.
    public AstarAgent(StateObservation stateObservation, ElapsedCpuTimer elapsedTime)
    {
        super(stateObservation, elapsedTime);
        //initialize path finder (A star algorithm) ignored obstracle objects
        ArrayList<Integer> obstracleTypes = new ArrayList<>();
        obstracleTypes.add(stateObservation.getObservationGrid()[0][0].get(0).itype);
        pathFinder = new PathFinder(obstracleTypes);
        pathFinder.run(stateObservation);

        //initialize game parameter
        step_count = 0;

        //initialize list of game objects
        itemsList = stateObservation.getImmovablePositions();
        portalsList = stateObservation.getPortalsPositions();
        npcsList = stateObservation.getNPCPositions();
    }

    public int FindBestItem(StateObservation stateObservation, ElapsedCpuTimer elapsedTime){
        //use manhattan distance to closest object
        return 0;
    }

    public int FindBestPortal(StateObservation stateObservation, ElapsedCpuTimer elapsedTime){
        //use manhattan distance to closest object
        return 0;
    }

    public int FindBestNpc(StateObservation stateObservation, ElapsedCpuTimer elapsedTime){
        //use manhattan distance to closest object
        return 0;
    }

    public Types.ACTIONS walkToItem(StateObservation stateObservation, ElapsedCpuTimer elapsedTime){
        itemsList = stateObservation.getImmovablePositions();
        if(itemsList != null){
            Vector2d playerPosition = stateObservation.getAvatarPosition();
            //upgrade best item should collect first(index get)
            Vector2d goalPosition = itemsList[0].get(0).position;

            //need to interact with item
            if(playerPosition.equals(goalPosition)){
                return Types.ACTIONS.ACTION_USE;
            }
            Types.ACTIONS action = getActionToDestination(playerPosition,goalPosition);
            if(action != null){
                return action;
            }
        }
        return null;
    }

    public Types.ACTIONS walkToPortal(StateObservation stateObservation, ElapsedCpuTimer elapsedTime){
        portalsList = stateObservation.getPortalsPositions();
        if(portalsList != null){
            Vector2d playerPosition = stateObservation.getAvatarPosition();

            //upgrade later (return best index)
            Vector2d goalPosition = portalsList[0].get(0).position;

            //need to check that portal is not the key to win the game
            if(playerPosition.equals(goalPosition)){
                return null;
            }
            Types.ACTIONS action = getActionToDestination(playerPosition,goalPosition);
            if(action != null){
                return action;
            }

        }
        return null;
    }

    public Types.ACTIONS walkToNpc(StateObservation stateObservation, ElapsedCpuTimer elapsedTime){
        npcsList = stateObservation.getNPCPositions();
        if(npcsList != null){
            Vector2d playerPosition = stateObservation.getAvatarPosition();
            //upgrade later (return best index)
            Vector2d goalPosition = npcsList[0].get(0).position;

            Types.ACTIONS action = getActionToDestination(playerPosition, goalPosition);
            if(action != null) {
                return action;
            }
        }
        return null;
    }

    //use path finder (A star algorithm to find the action to destination)
    public Types.ACTIONS getActionToDestination(Vector2d playerPos, Vector2d goalPos){
        boolean ATK_USE_ABLE = stateObservation.getAvailableActions().contains(Types.ACTIONS.ACTION_USE);
        if(ATK_USE_ABLE){
            return Types.ACTIONS.ACTION_USE;
        }
        ArrayList<Node> path = pathFinder.getPath(playerPos, goalPos);
        if(path != null) {
            Vector2d nextPath = path.get(0).position;
            return getActionFromPosition(playerPos, nextPath);
        }
        return null;
    }

    //get action from vector of the next step
    public Types.ACTIONS getActionFromPosition(Vector2d playerPos, Vector2d goalPos){
        Vector2d moveVector = new Vector2d(goalPos.x - playerPos.x, goalPos.y - playerPos.y);
        return Types.ACTIONS.fromVector(moveVector);
    }

    //core function to return action to controller
    @Override
    public Types.ACTIONS run(StateObservation stateObservation, ElapsedCpuTimer elapsedTime){

        Types.ACTIONS action = walkToItem(stateObservation,elapsedTime);

        if(action != null){
            return action;
        }

        action = walkToPortal(stateObservation,elapsedTime);
        if(action != null){
            return action;
        }

        action = walkToNpc(stateObservation,elapsedTime);
        if(action != null){
            return action;
        }

        return null;
    }
}
import { DO_LOGIN, 
  SET_SPECTATING, 
  SET_PLAYER_NAME, 
  SET_COMMUNITY_CARDS, 
  SET_OPPONENT_CARDS, 
  SET_MY_CARDS, 
  SET_PLAYER_ORDER,
  SET_CURRENT_BET,
 } from "../actions";

export interface AppState {
  playerName: string;
  opponentCards:{},
  communityCards: string[];
  myCards: string[];
  playerOrder: string[];
  currentBet: string | undefined;
  spectating: boolean,
  loggedIn: boolean,
  balances: {[key:string]: string},
}

const initalState: AppState = {
  playerName: "anonymousPlayer",
  opponentCards:{},
  communityCards: [],
  myCards:[],
  balances: {"cdurr": "1.26", "mheatzig": "2.46"},
  playerOrder: ["cdurr", "mheatzig"],
  currentBet: undefined,
  spectating: true,
  loggedIn: false,
};

export function rootReducer(state = initalState, action: any) {
  switch (action.type) {
    case SET_PLAYER_NAME:
      return {...state, playerName: action.payload};
    case SET_COMMUNITY_CARDS:
      return {...state, communityCards: action.payload};
    case SET_OPPONENT_CARDS:
      return {...state, opponentCards: action.payload};
    case SET_MY_CARDS:
      return {...state, myCards: action.payload};
    case SET_PLAYER_ORDER:
      return {...state, playerOrder: action.payload};
    case SET_CURRENT_BET:
      return {...state, currentBet: action.payload};
    case SET_SPECTATING:
      return {...state, spectating: action.payload};
    case DO_LOGIN:
      return {...state, loggedIn: true};
    default:
      return state
  }
}
export const SET_PLAYER_NAME = 'SET_PLAYER_NAME';
export const SET_COMMUNITY_CARDS = 'SET_COMMUNITY_CARDS';
export const SET_OPPONENT_CARDS = 'SET_OPPONENT_CARDS';
export const SET_MY_CARDS = 'SET_MY_CARDS';
export const SET_PLAYER_ORDER = 'SET_PLAYER_ORDER';
export const SET_CURRENT_BET = 'SET_CURRENT_BET';
export const SET_SPECTATING = 'SET_SPECTATING';
export const SET_BALANCES = 'SET_BALANCES';
export const DO_LOGIN = 'DO_LOGIN';

const setPlayerName = (playerName: string) => ({
  type: SET_PLAYER_NAME, 
  payload:playerName,
});

const setCommunityCards = (communityCards: string[]) => ({
  type: SET_COMMUNITY_CARDS, 
  payload: communityCards
});

const setOpponentCards = (opponentCards: {}) => ({
  type: SET_OPPONENT_CARDS, 
  payload: opponentCards
});

const setMyCards = (myCards: string[]) => ({
  type: SET_MY_CARDS, 
  payload: myCards
});

const setPlayerOrder = (playerOrder: string[]) => ({
  type: SET_PLAYER_ORDER, 
  payload: playerOrder
});

const setCurrentBet = (currentBet: number) => ({
  type: SET_CURRENT_BET, 
  payload: currentBet
});

const setSpectating = (spectating: boolean) => ({
  type: SET_SPECTATING, 
  payload: spectating
});

const setBalances = (balances: {}) => ({
  type: SET_BALANCES, 
  payload: balances
});

const doLogin = () => ({
  type: DO_LOGIN, 
});

export const actionCreators: {} = {
  setPlayerName, 
  setCommunityCards, 
  setOpponentCards, 
  setMyCards,
  setPlayerOrder, 
  setCurrentBet, 
  setSpectating,
  setBalances,
  doLogin,
}
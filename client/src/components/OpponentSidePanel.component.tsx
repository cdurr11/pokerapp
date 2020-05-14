import React, {Component} from 'react';
import OpponentTile from "./OpponentTile.component"
import '../css/sidePanel.css'
import { AppState } from '../redux/reducers/reducer'
import { connect } from 'react-redux';

interface OpponentSidePanelProps {
  playerOrder: string[];
  opponentCards: {};
  spectating: boolean;
  playerName: string;
  balances: {[key:string]: string};
}

function mapStateToProps(state: AppState): OpponentSidePanelProps {
  return {
    playerOrder: state.playerOrder,
    opponentCards: state.opponentCards,
    spectating: state.spectating,
    playerName: state.playerName,  
    balances: state.balances,
  };
}

class OpponentSidePanel extends Component<OpponentSidePanelProps> {
  makeOpponentTileDOMS(): JSX.Element[] {
    const playerOrderReversed: string[] = this.props.playerOrder.map(x => x);
    let opponentTiles: JSX.Element[] = [];
    playerOrderReversed.reverse();
    if (!this.props.spectating) {
      let currentPlayerIndex: number = playerOrderReversed.
        findIndex(player => player === this.props.playerName);
      if (currentPlayerIndex === -1) {
        throw "Didn't Find Current Player";
      }
      let moddedIndex : number;
      for (let i = (currentPlayerIndex + 1); i < playerOrderReversed.length - 1; i++) {
        let moddedIndex = i % playerOrderReversed.length;
        opponentTiles.push(
        <OpponentTile 
          cards={this.props.opponentCards}
          name={playerOrderReversed[i]}
          balances={this.props.balances}
        />);
      }
      
    } else {
      playerOrderReversed.forEach((element, i) => {
        opponentTiles.push(
          <OpponentTile 
            cards={this.props.opponentCards}
            name={playerOrderReversed[i]}
            balances={this.props.balances}
          />);
      });
    }
    return opponentTiles;
    
  }

  render(): JSX.Element {
    return (
      <div className="border-elem-left">
        <div className="ctn-opponent-sidepanel">
          {this.makeOpponentTileDOMS()}
        </div>
      </div>
    );
  }
}

export default connect(mapStateToProps)(OpponentSidePanel);
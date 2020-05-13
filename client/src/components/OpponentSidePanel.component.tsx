import React, {Component} from 'react';
import OpponentTile from "./OpponentTile.component"
import '../css/sidePanel.css'

interface OpponentSidePanelProps {
  playerOrder: string[];
  playerCards: {};
  spectating: boolean;
  playerName: string;
}

class OpponentSidePanel extends Component<OpponentSidePanelProps> {
  makeOpponentTileDOMS() {
    const playerOrderReversed: string[] = this.props.playerOrder.map(x => x);
    let opponentTiles: JSX.Element[] = [];
    playerOrderReversed.reverse();
    if (this.props.spectating) {
      let currentPlayerIndex: number = playerOrderReversed.
        findIndex(player => player === this.props.playerName);
      if (currentPlayerIndex === -1) {
        throw "Didn't Find Current Player";
      }
      let moddedIndex : number;
      for (let i = (currentPlayerIndex + 1); i < playerOrderReversed.length - 1; i++) {
        let moddedIndex = i % playerOrderReversed.length;
        opponentTiles.push(<OpponentTile cards={}, name={}/>);
      }
    } else {

    }
    
  }

  render(): JSX.Element {
    return (
      <div className="border-elem-left">
        <div className="ctn-opponent-sidepanel">
          <OpponentTile/>
          <OpponentTile/>
          <OpponentTile/>
          <OpponentTile/>
          <OpponentTile/>
        </div>
      </div>
    );
  }
}

export default OpponentSidePanel;
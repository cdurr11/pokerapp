import React, {Component} from 'react';
// import card from '../static/10CS.svg';
var card = require('../static/10CS.svg');

interface OpponentTileProps {
  cards: {};
  name: string;
  balances: {[key:string]: string};
}

class OpponentTile extends Component<OpponentTileProps> {

  render(): JSX.Element {
    return (
      <div className="ctn-opponent-tile">
        <div className="ctn-opponent-moneyname">
          <p className="mod-opponent-name">{this.props.name}</p>
          <p className="mod-opponent-money">{"$" + this.props.balances[this.props.name]}</p>
        </div>  
        <img className="opponent-card" alt="" src={card}/>
        <img className="opponent-card-right" alt="" src={card}/>
      </div>
    );
  }
}

export default OpponentTile;
import React, {Component} from 'react';
import card from '../static/10CS.svg';

class OpponentTile extends Component {

  render(): JSX.Element {
    return (
      <div className="ctn-opponent-tile">
        <div className="ctn-opponent-moneyname">
          <p className="mod-opponent-name">Cody Durr</p>
          <p className="mod-opponent-money">$100</p>
        </div>  
        <img className="opponent-card" alt="" src={card}/>
        <img className="opponent-card-right" alt="" src={card}/>
      </div>
    );
  }
}

export default OpponentTile;
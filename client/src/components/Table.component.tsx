import React, {Component} from 'react';
import CommunityCards from "./CommunityCards.component"
import MyHand from "./MyHand.component"
import pokerTableImg from '../static/pokertable.svg';
import '../css/table.css';

class Table extends Component {

  render(): JSX.Element {
    return (
      <div className="ctn-table">
        <img className="" alt="" src={pokerTableImg}/>
        <button className="call-button mod-unselectable">Call</button>
        <button className="check-button mod-unselectable">Check</button>
        <button className="fold-button mod-unselectable">Fold</button>
        <button disabled={true} className="raise-button mod-unselectable">Raise</button>
        <input placeholder="RAISE AMT" className="raise-form"></input>
        <p className="player-balance mod-unselectable">$20</p>
        <p className="pot mod-unselectable">$20.00</p>
        <MyHand/>
        <CommunityCards/>
      </div>
    );
  }
}
export default Table;
import React, {Component} from 'react';
import card from '../static/10C.svg';

class CommunityCards extends Component {

  render(): JSX.Element {
    return (
      <div className="ctn-ctr-cards">
        <img alt="" src={card}/>
        <img className="mod-right-card" alt="" src={card}/>
        <img className="mod-right-card" alt="" src={card}/>
        <img className="mod-right-card" alt="" src={card}/>
        <img className="mod-right-card" alt="" src={card}/>
      </div>
    );
  }
}

export default CommunityCards;
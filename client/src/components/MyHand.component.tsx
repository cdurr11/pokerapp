import React, {Component} from 'react';
import card from '../static/10C.svg';

class MyHand extends Component {
  render() {
    return (
      <div className="ctn-my-hand">
        <img alt="" src={card}/>
        <img className="mod-right-card" alt="" src={card}/>
    </div>
    );
  }
}
export default MyHand;

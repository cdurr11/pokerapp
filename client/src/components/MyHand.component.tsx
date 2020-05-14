import React, {Component} from 'react';
// import card from '../static/10C.svg';
var card = require('../static/10C.svg');



interface MyHandProps {
  myCards: string[];
};

class MyHand extends Component<MyHandProps> {

  makeCardsDOM() {
    var cardsDOM : JSX.Element[] = [];
    this.props.myCards.forEach((e, index) => {
      cardsDOM.push(<img className="mod-right-card" key={index} alt="" src={card}></img>)
    });
    
    return cardsDOM;
  }
  render() {
    return (
      <div className="ctn-my-hand">
        {this.makeCardsDOM()}
      </div>
    );
  }
}
export default MyHand;

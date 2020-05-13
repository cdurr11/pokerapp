import React, {Component} from 'react';
import card from '../static/10C.svg';

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
        {/* <img alt="" src={card}/>
        <img className="mod-right-card" alt="" src={card}/> */}
        {this.makeCardsDOM()}
      </div>
    );
  }
}
export default MyHand;

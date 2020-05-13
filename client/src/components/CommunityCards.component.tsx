import React, {Component} from 'react';
import card from '../static/10C.svg';

interface CommunityCardsProps {
  communityCards: string[];
};

class CommunityCards extends Component<CommunityCardsProps> {

  makeCardsDOM() {
    var cardsDOM : JSX.Element[] = [];
    this.props.communityCards.forEach((e, index) => {
      cardsDOM.push(<img className="mod-right-card" key={index} alt="" src={card}></img>)
    });
    return cardsDOM;
  }

  render(): JSX.Element {
    return (
      <div className="ctn-ctr-cards">
        {this.makeCardsDOM()}
      </div>
    );
  }
}

export default CommunityCards;
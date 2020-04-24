import React, {Component} from 'react';
import pokerTableImg from './static/pokertable.svg';
import card from './static/10C.svg';
import './App.css';

class App extends Component {

  render():any {
    return (
      <div className="ctn-app">
        <div className="border-elem-left"/>
        <div className="ctn-middle">
          <div className="ctn-table">
            <img className="test" alt="" src={pokerTableImg}/>
  
            <div className="ctn-my-hand">
              <img alt="" src={card}/>
              <img className="mod-right-card" alt="" src={card}/>
            </div>
  
            <div className="ctn-ctr-cards">
              <img alt="" src={card}/>
              <img className="mod-right-card" alt="" src={card}/>
              <img className="mod-right-card" alt="" src={card}/>
              <img className="mod-right-card" alt="" src={card}/>
              <img className="mod-right-card" alt="" src={card}/>
            </div>
          </div>
          <div className="bottom-border-elem"/>
        </div>
        <div className="border-elem"></div>
      </div>
    );
  }
  
}

export default App;

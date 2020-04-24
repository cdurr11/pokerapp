import React, {Component} from 'react';
import '../css/app.css';
import OpponentSidePanel from './OpponentSidePanel.component.tsx';
import Table from "./Table.component.tsx"

class App extends Component {

  render(): JSX.Element {
    return (
      <div className="ctn-app">
        <OpponentSidePanel/>
        <div className="ctn-middle">
          <Table/>
          <div className="bottom-border-elem"/>
        </div>
        <div className="border-elem"></div>
      </div>
    );
  }
  
}

export default App;
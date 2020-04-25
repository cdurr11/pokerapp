import React, {Component} from 'react';
import OpponentTile from "./OpponentTile.component"
import '../css/sidePanel.css'

class OpponentSidePanel extends Component {

  render(): JSX.Element {
    return (
      <div className="border-elem-left">
        <div className="ctn-opponent-sidepanel">
          <OpponentTile/>
          <OpponentTile/>
          <OpponentTile/>
          <OpponentTile/>
          <OpponentTile/>
        </div>
      </div>
    );
  }
}

export default OpponentSidePanel;
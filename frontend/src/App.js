import './App.css';
import GetMetrics from './components/getmetrics'
import PostMetrics from './components/postmetrics'
import React, {Component} from 'react';


class App extends Component {

    state = {
        metrics: []
    }

    render() {
        return (
            <div className="App">
                <header className="App-header">
                    Metrics frontend
                </header>
                <div>
                    <GetMetrics/>
                </div>
                <div>
                    <PostMetrics/>
                </div>
            </div>
        );
    }
}

export default App;

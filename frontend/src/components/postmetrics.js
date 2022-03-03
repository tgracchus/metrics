import React from 'react'
import environment from './environment'

class PostMetrics extends React.Component {
    constructor(props) {
        super(props);
        this.state = {metric: '', timestamp: '', value: '', data: [], error:undefined};
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.id]: event.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();

        const headers = new Headers();
        headers.append("Content-Type", "application/json");

        const body = JSON.stringify({
            "metrics": [
                {
                    "metric": `${this.state.metric}`,
                    "points": [
                        {
                            "timestamp": `${this.state.timestamp}`,
                            "value": `${this.state.value}`,
                        }
                    ]
                }
            ]
        })

        const requestOptions = {
            method: 'POST',
            headers: headers,
            body: body,
            redirect: 'follow'
        };



        fetch(`http://${environment.backend}:8080/ingest`, requestOptions)
            .then(response => {
                if(!response.ok){
                    return response.text().then(text => { throw new Error(text) })
                }
                return response.json()
            })
            .then((data) => {
                this.setState({error:undefined})
                console.log('data', data)
            })
            .catch(error => {
                this.setState({error:error.toString()})
                console.log('error', error)
            });
    }

    render() {
        return (
            <div>
                <div>
                    <form onSubmit={this.handleSubmit}>
                        <label>
                            Metric Name:
                            <input id="metric" type="text" name="metric" value={this.state.metric} onChange={this.handleChange}/>
                        </label>
                        <label>
                            Timestamp:
                            <input id="timestamp" type="text" name="timestamp" value={this.state.timestamp}
                                   onChange={this.handleChange}/>
                        </label>
                        <label>
                            Value:
                            <input id="value" type="text" name="value" value={this.state.value}
                                   onChange={this.handleChange}/>
                        </label>
                        <input type="submit" value="Submit"/>
                        {this.state.error && <div className="App-error"> {this.state.error} </div>}
                    </form>
                </div>
            </div>
        );
    }
}

export default PostMetrics

import React from 'react'
import {CartesianGrid, Legend, Line, LineChart, Tooltip, XAxis, YAxis} from "recharts";

const Timeline = ({ metrics }) => {
    return (
        <LineChart
            width={1024}
            height={500}
            data={metrics.points}
            margin={{
                top: 5,
                right: 30,
                left: 20,
                bottom: 5
            }}
        >
            <CartesianGrid strokeDasharray="3 3"/>
            <XAxis dataKey="timestamp"/>
            <YAxis/>
            <Tooltip/>
            <Legend/>
            <Line
                type="monotone"
                dataKey="value"
                stroke="#8884d8"
                name={metrics.metric}
                activeDot={{r: 8}}
            />
        </LineChart>
    )
};

export default Timeline

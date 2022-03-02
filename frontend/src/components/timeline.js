import React from 'react'
import {CartesianGrid, Legend, Line, LineChart, Tooltip, XAxis, YAxis} from "recharts";

const Timeline = ({ metrics }) => {
    return (
        <LineChart
            width={500}
            height={300}
            data={metrics}
            margin={{
                top: 5,
                right: 30,
                left: 20,
                bottom: 5
            }}
        >
            <CartesianGrid strokeDasharray="3 3"/>
            <XAxis dataKey="key"/>
            <YAxis/>
            <Tooltip/>
            <Legend/>
            <Line
                type="monotone"
                dataKey="value"
                stroke="#8884d8"
                activeDot={{r: 8}}
            />
        </LineChart>
    )
};

export default Timeline

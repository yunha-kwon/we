import React from "react";
import "./Snowflake.css";

const Snowflake: React.FC<{ style: React.CSSProperties }> = ({ style }) => {
  return (
    <p className="snow-flake" style={style}>
      {"\u2745"}
    </p>
  );
};

const makeSnowFlakes = () => {
  const numSnowflakes = 100;
  return Array.from({ length: numSnowflakes }, (_, i) => {
    const animationDelay = `${(Math.random() * 16).toFixed(2)}s`;
    const fontSize = `${Math.floor(Math.random() * 10) + 15}px`;
    const left = `${Math.random() * 100}vw`;
    const style = {
      animationDelay,
      fontSize,
      left,
    };
    return <Snowflake key={i} style={style} />;
  });
};

const FallingSnow: React.FC = () => {
  return <div className="snow-container">{makeSnowFlakes()}</div>;
};

export default FallingSnow;

import { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [showTables, setShowTables] = useState(false);
  const [jsonData, setJsonData] = useState(null);
  const [selectedButtonIndex, setSelectedButtonIndex] = useState(null);

  useEffect(() => {
    // Simulating API call with JSON data
    const fetchData = async () => {
      // Replace this with your actual API call
      const response = await fetch('your_api_endpoint');
      const data = await response.json();
      setJsonData(data);
      setShowTables(true);
    };

    fetchData();
  }, []);

  // Function to handle button click
  const handleClick = (index) => {
    setShowTables(true);
    setSelectedButtonIndex(index);
  };

  // Sample JSON data
  const buttonsData = [
    { label: 'Button 1', contentKey: 'content1' },
    { label: 'Button 2', contentKey: 'content2' },
    { label: 'Button 3', contentKey: 'content3' },
    { label: 'Button 4', contentKey: 'content4' },
    { label: 'Button 5', contentKey: 'content5' },
  ];

  return (
    <div className="App">
      <div className="mt-8 flex flex-col items-center">
        {/* Render buttons dynamically */}
        {buttonsData.map((button, index) => (
          <button
            key={index}
            onClick={() => handleClick(index)}
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-3 px-6 rounded-lg mb-4"
          >
            {button.label}
          </button>
        ))}
      </div>

      {/* Render tables based on selected button */}
      {showTables && jsonData && selectedButtonIndex !== null && (
        <div className="mt-8">
          <table className="table-auto">
            <thead>
              <tr>
                <th className="px-4 py-2">Header 1</th>
                <th className="px-4 py-2">Header 2</th>
                <th className="px-4 py-2">Header 3</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                {/* Access data based on selectedButtonIndex */}
                {jsonData[buttonsData[selectedButtonIndex].contentKey].map((item, index) => (
                  <td key={index} className="border px-4 py-2">{item}</td>
                ))}
              </tr>
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default App;

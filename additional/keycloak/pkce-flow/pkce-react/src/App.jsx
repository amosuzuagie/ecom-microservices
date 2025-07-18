import { useContext, useEffect, useState } from "react";
import { AuthContext } from "react-oauth2-code-pkce";

function App() {
  const { token, tokenData, logIn, logOut, isAuthenticated } =
    useContext(AuthContext);

  const [message, setMessage] = useState("");

  useEffect(() => {
    if (token) {
      console.log("Token available: " + token);
      fetchHello();
    }
  }, [token]);

  const fetchHello = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/home", {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "text/plain; charset=utf-8",
        },
      });
      if (res.ok) {
        const text = res.text();
        setMessage(text);
      }
    } catch (error) {
      console.warn("API ERROR!: ", res.status);
      setMessage("API ERROR!");
    }
  };

  return (
    <div style={{ padding: "2rem" }}>
      <h1>OAuth2 PKCE Demo</h1>
      <div>
        {!token ? (
          <button onClick={() => logIn()}>Login</button>
        ) : (
          <>
            <button onClick={() => logOut()}>Logout</button>
            <div>
              <h3>Message From API</h3>
              <p>{message}</p>
              <h3>Token Data</h3>
              <pre>{JSON.stringify(tokenData, null, 2)}</pre>
            </div>
          </>
        )}
      </div>
    </div>
  );
}

export default App;

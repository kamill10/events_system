import {BrowserRouter} from "react-router-dom";
import RouterComponent from "./router/RouterComponent.tsx";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { AccountStateContextProvider } from "./context/AccountContext.tsx";

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AccountStateContextProvider>
        <BrowserRouter>
          <RouterComponent></RouterComponent>
        </BrowserRouter>
      </AccountStateContextProvider>
    </QueryClientProvider>
  )
}

export default App

import {BrowserRouter} from "react-router-dom";
import RouterComponent from "./router/RouterComponent.tsx";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <RouterComponent></RouterComponent>
      </BrowserRouter>
    </QueryClientProvider>
  )
}

export default App

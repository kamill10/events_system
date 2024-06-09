import { SpeakersWithNumberOfElements } from "../types/Speaker.ts";
import { createContext, ReactNode, useContext, useState } from "react";

interface ManageSpeakers {
  speakers: SpeakersWithNumberOfElements | null;
  setSpeakers: (speakers: SpeakersWithNumberOfElements) => void;
  isFetching: boolean;
  setIsFetching: (state: boolean) => void;
}

const SpeakersStateContext = createContext<ManageSpeakers | null>(null);

export const SpeakersStateContextProvider = ({
  children,
}: {
  children: ReactNode;
}) => {
  const [speakers, setSpeakers] = useState<SpeakersWithNumberOfElements | null>(
    null,
  );
  const [isFetching, setIsFetching] = useState(false);

  return (
    <SpeakersStateContext.Provider
      value={{
        speakers,
        setSpeakers,
        isFetching,
        setIsFetching,
      }}
    >
      {children}
    </SpeakersStateContext.Provider>
  );
};

export const useSpeakersState = () => {
  const speakersState = useContext(SpeakersStateContext);

  if (!speakersState) {
    throw new Error("You forgot about SpeakersStateContext");
  }

  return speakersState;
};

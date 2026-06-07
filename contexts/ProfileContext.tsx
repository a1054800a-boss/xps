// Powered by OnSpace.AI
import React, { createContext, useState, useEffect, ReactNode, useCallback } from 'react';
import {
  SpoofProfile,
  loadProfiles,
  addProfile,
  updateProfile,
  deleteProfile,
} from '@/services/profileService';

interface ProfileContextType {
  profiles:      SpoofProfile[];
  loading:       boolean;
  createProfile: (p: SpoofProfile) => Promise<void>;
  editProfile:   (p: SpoofProfile) => Promise<void>;
  removeProfile: (id: string) => Promise<void>;
  toggleProfile: (id: string) => Promise<void>;
  refresh:       () => Promise<void>;
}

export const ProfileContext = createContext<ProfileContextType | undefined>(undefined);

export function ProfileProvider({ children }: { children: ReactNode }) {
  const [profiles, setProfiles] = useState<SpoofProfile[]>([]);
  const [loading,  setLoading]  = useState(true);

  const refresh = useCallback(async () => {
    setLoading(true);
    const list = await loadProfiles();
    setProfiles(list);
    setLoading(false);
  }, []);

  useEffect(() => {
    refresh();
  }, [refresh]);

  const createProfile = async (p: SpoofProfile) => {
    await addProfile(p);
    setProfiles(prev => [...prev, p]);
  };

  const editProfile = async (p: SpoofProfile) => {
    await updateProfile(p);
    setProfiles(prev => prev.map(x => (x.id === p.id ? p : x)));
  };

  const removeProfile = async (id: string) => {
    await deleteProfile(id);
    setProfiles(prev => prev.filter(x => x.id !== id));
  };

  const toggleProfile = async (id: string) => {
    const p = profiles.find(x => x.id === id);
    if (!p) return;
    const updated = { ...p, enabled: !p.enabled };
    await updateProfile(updated);
    setProfiles(prev => prev.map(x => (x.id === id ? updated : x)));
  };

  return (
    <ProfileContext.Provider value={{ profiles, loading, createProfile, editProfile, removeProfile, toggleProfile, refresh }}>
      {children}
    </ProfileContext.Provider>
  );
}

// Powered by OnSpace.AI
import { useContext } from 'react';
import { ProfileContext } from '@/contexts/ProfileContext';

export function useProfiles() {
  const ctx = useContext(ProfileContext);
  if (!ctx) throw new Error('useProfiles must be used within ProfileProvider');
  return ctx;
}

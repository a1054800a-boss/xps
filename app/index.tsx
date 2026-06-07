// Powered by OnSpace.AI
import React, { useState } from 'react';
import {
  View, Text, StyleSheet, FlatList,
  Pressable, StatusBar, Platform,
} from 'react-native';
import { Image } from 'expo-image';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { MaterialIcons } from '@expo/vector-icons';
import { useAlert } from '@/template';
import { useProfiles } from '@/hooks/useProfiles';
import { SpoofProfile } from '@/services/profileService';
import { ProfileCard }   from '@/components/feature/ProfileCard';
import { ProfileEditor } from '@/components/feature/ProfileEditor';
import { GlowButton }    from '@/components/ui/GlowButton';
import { Colors, FontSize, FontWeight, Radius, Spacing } from '@/constants/theme';

export default function HomeScreen() {
  const insets = useSafeAreaInsets();
  const { showAlert } = useAlert();
  const { profiles, loading, createProfile, editProfile, removeProfile, toggleProfile } = useProfiles();

  const [editorOpen,    setEditorOpen]    = useState(false);
  const [editingProfile, setEditingProfile] = useState<SpoofProfile | undefined>(undefined);

  const openCreate = () => { setEditingProfile(undefined); setEditorOpen(true); };
  const openEdit   = (p: SpoofProfile) => { setEditingProfile(p); setEditorOpen(true); };

  const handleSave = async (p: SpoofProfile) => {
    if (editingProfile) {
      await editProfile(p);
    } else {
      await createProfile(p);
    }
    setEditorOpen(false);
  };

  const handleDelete = (id: string) => {
    showAlert('Delete Profile', 'This profile will be permanently removed.', [
      { text: 'Cancel', style: 'cancel' },
      { text: 'Delete', style: 'destructive', onPress: () => removeProfile(id) },
    ]);
  };

  const activeCount = profiles.filter(p => p.enabled).length;

  return (
    <View style={[styles.root, { paddingTop: insets.top }]}>
      <StatusBar barStyle="light-content" backgroundColor={Colors.bg} />

      {/* Header */}
      <View style={styles.header}>
        <View>
          <Text style={styles.appTitle}>
            <Text style={styles.titleGreen}>X</Text>SPOOF
          </Text>
          <Text style={styles.subtitle}>LSposed Companion Config</Text>
        </View>
        <View style={styles.statsRow}>
          <View style={styles.statBadge}>
            <Text style={styles.statNum}>{profiles.length}</Text>
            <Text style={styles.statLabel}>Profiles</Text>
          </View>
          <View style={[styles.statBadge, activeCount > 0 && styles.activeBadge]}>
            <Text style={[styles.statNum, activeCount > 0 && styles.activeNum]}>{activeCount}</Text>
            <Text style={styles.statLabel}>Active</Text>
          </View>
        </View>
      </View>

      {/* List */}
      <FlatList
        data={profiles}
        keyExtractor={p => p.id}
        renderItem={({ item }) => (
          <ProfileCard
            profile={item}
            onEdit={openEdit}
            onDelete={handleDelete}
            onToggle={toggleProfile}
          />
        )}
        ListEmptyComponent={!loading ? <EmptyState onAdd={openCreate} /> : null}
        contentContainerStyle={[styles.list, profiles.length === 0 && styles.listEmpty]}
        showsVerticalScrollIndicator={false}
      />

      {/* FAB */}
      {profiles.length > 0 ? (
        <Pressable
          onPress={openCreate}
          style={({ pressed }) => [
            styles.fab,
            { bottom: insets.bottom + Spacing.lg },
            pressed && { opacity: 0.8, transform: [{ scale: 0.96 }] },
          ]}
        >
          <MaterialIcons name="add" size={28} color={Colors.bg} />
        </Pressable>
      ) : null}

      {/* Editor modal */}
      <ProfileEditor
        visible={editorOpen}
        initial={editingProfile}
        onSave={handleSave}
        onCancel={() => setEditorOpen(false)}
      />
    </View>
  );
}

function EmptyState({ onAdd }: { onAdd: () => void }) {
  return (
    <View style={emptyStyles.wrap}>
      <Image
        source={require('@/assets/images/hero-spoof.png')}
        style={emptyStyles.hero}
        contentFit="contain"
        transition={300}
      />
      <Text style={emptyStyles.title}>No Profiles Yet</Text>
      <Text style={emptyStyles.sub}>
        Create a spoof profile to configure fake device info, IPs, and Android identifiers for any app.
      </Text>
      <GlowButton label="+ Create First Profile" onPress={onAdd} size="lg" style={emptyStyles.btn} />

      <View style={emptyStyles.noticeWrap}>
        <MaterialIcons name="info-outline" size={13} color={Colors.textMuted} />
        <Text style={emptyStyles.notice}>
          Requires LSposed / Xposed Framework installed on a rooted or patched device.
        </Text>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  root:      { flex: 1, backgroundColor: Colors.bg },
  header:    { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', paddingHorizontal: Spacing.md, paddingBottom: Spacing.md, borderBottomWidth: 1, borderColor: Colors.border },
  appTitle:  { fontSize: FontSize.hero, fontWeight: FontWeight.bold, color: Colors.textPrimary, letterSpacing: 2 },
  titleGreen:{ color: Colors.primary },
  subtitle:  { fontSize: FontSize.xs, color: Colors.textMuted, letterSpacing: 0.5 },
  statsRow:  { flexDirection: 'row', gap: Spacing.sm },
  statBadge: { backgroundColor: Colors.bgCard, borderRadius: Radius.sm, padding: Spacing.sm, alignItems: 'center', borderWidth: 1, borderColor: Colors.border, minWidth: 52 },
  activeBadge:{ borderColor: Colors.primaryDim, backgroundColor: Colors.primaryGlow },
  statNum:   { fontSize: FontSize.lg, fontWeight: FontWeight.bold, color: Colors.textPrimary },
  activeNum: { color: Colors.primary },
  statLabel: { fontSize: 9, color: Colors.textMuted, letterSpacing: 0.5 },
  list:      { paddingHorizontal: Spacing.md, paddingTop: Spacing.md },
  listEmpty: { flexGrow: 1 },
  fab:       {
    position: 'absolute', right: Spacing.md,
    width: 56, height: 56, borderRadius: 28,
    backgroundColor: Colors.primary,
    alignItems: 'center', justifyContent: 'center',
    shadowColor: Colors.primary,
    ...Platform.select({
      ios:     { shadowOffset: { width: 0, height: 4 }, shadowOpacity: 0.5, shadowRadius: 12 },
      android: { elevation: 8 },
    }),
  },
});

const emptyStyles = StyleSheet.create({
  wrap:     { flex: 1, alignItems: 'center', justifyContent: 'center', padding: Spacing.xl },
  hero:     { width: 200, height: 280, marginBottom: Spacing.lg },
  title:    { fontSize: FontSize.xl, fontWeight: FontWeight.bold, color: Colors.textPrimary, marginBottom: Spacing.sm },
  sub:      { fontSize: FontSize.sm, color: Colors.textSecondary, textAlign: 'center', lineHeight: 22, marginBottom: Spacing.xl },
  btn:      { width: '100%', marginBottom: Spacing.lg },
  noticeWrap:{ flexDirection: 'row', alignItems: 'flex-start', gap: 6, marginTop: Spacing.sm },
  notice:   { fontSize: FontSize.xs, color: Colors.textMuted, flex: 1, lineHeight: 18 },
});

import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useTheme } from '../../../hooks/ThemeContext';
import { LearningPlan } from '../types';
import { LearningPlanCard } from './LearningPlanCard';
import { request } from '../../../utils/api';
import '../styles.scss';

// Mock data to use when the API fails
const MOCK_PLANS: LearningPlan[] = [
  {
    id: '1',
    title: 'Web Development with React',
    description: 'Learn modern web development using React. This comprehensive plan covers everything from fundamentals to advanced concepts.',
    subject: 'Technology',
    topics: [
      { id: 't1', title: 'JavaScript Fundamentals', completed: true },
      { id: 't2', title: 'React Basics', completed: true },
      { id: 't3', title: 'State Management', completed: false },
      { id: 't4', title: 'React Hooks', completed: false },
    ],
    resources: [
      { id: 'r1', title: 'React Documentation', url: 'https://reactjs.org/docs/getting-started.html', type: 'link' },
      { id: 'r2', title: 'React Crash Course', url: 'https://www.youtube.com/watch?v=w7ejDZ8SWv8', type: 'video' },
      { id: 'r3', title: 'React Cheatsheet', url: 'https://reactcheatsheet.com/', type: 'document' },
    ],
    completionPercentage: 50,
    estimatedDays: 21,
    followers: 42,
    createdAt: new Date().toISOString(),
    user: {
      id: 'u1',
      name: 'Sarah Miller',
      username: 'sarahmiller',
      profilePicture: 'https://i.pravatar.cc/150?img=32'
    },
    following: false
  },
  {
    id: '2',
    title: 'Mastering Basic Trigonometry',
    description: 'A step-by-step guide to understanding the fundamentals of trigonometry, from the unit circle to the laws of sines and cosines.',
    subject: 'Maths',
    topics: [
      { id: 't5', title: 'Introduction to Trigonometry', completed: true },
      { id: 't6', title: 'The Unit Circle', completed: true },
      { id: 't7', title: 'Trigonometric Functions', completed: true },
      { id: 't8', title: 'Solving Triangles', completed: false },
    ],
    resources: [
      { id: 'r4', title: 'Khan Academy Trigonometry', url: 'https://www.khanacademy.org/math/trigonometry', type: 'link' },
      { id: 'r5', title: 'Trigonometry Formula Sheet', url: 'https://example.com/trig-formulas.pdf', type: 'document' },
    ],
    completionPercentage: 75,
    estimatedDays: 14,
    followers: 67,
    createdAt: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString(),
    user: {
      id: 'u2',
      name: 'Alex Johnson',
      username: 'alexj',
      profilePicture: 'https://i.pravatar.cc/150?img=11'
    },
    following: true
  },
  {
    id: '3',
    title: 'Java Programming: From Basics to Advanced',
    description: 'Comprehensive Java learning path covering everything from syntax to advanced concepts like multithreading and modern APIs.',
    subject: 'Technology',
    topics: [
      { id: 't9', title: 'Java Syntax and Basics', completed: true },
      { id: 't10', title: 'Object-Oriented Programming', completed: false },
      { id: 't11', title: 'Collections Framework', completed: false },
      { id: 't12', title: 'Multithreading', completed: false },
    ],
    resources: [
      { id: 'r6', title: 'Java Documentation', url: 'https://docs.oracle.com/en/java/', type: 'link' },
      { id: 'r7', title: 'Java Video Course', url: 'https://example.com/java-course', type: 'video' },
    ],
    completionPercentage: 25,
    estimatedDays: 30,
    followers: 95,
    createdAt: new Date(Date.now() - 14 * 24 * 60 * 60 * 1000).toISOString(),
    user: {
      id: 'u3',
      name: 'David Wilson',
      username: 'davidw',
      profilePicture: 'https://i.pravatar.cc/150?img=68'
    },
    following: false
  }
];

interface LearningPlanListProps {
  filter?: 'all' | 'my';
  userId?: string;
}

export const LearningPlanList = ({ filter = 'all', userId }: LearningPlanListProps) => {
  const { theme } = useTheme();
  const [plans, setPlans] = useState<LearningPlan[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [useMockData, setUseMockData] = useState(false);

  useEffect(() => {
    setLoading(true);
    request<LearningPlan[]>({
      endpoint: `/api/v1/learning-plans${filter === 'my' ? `?userId=${userId}` : ''}`,
      method: 'GET',
      onSuccess: (data) => {
        setPlans(data);
        setLoading(false);
        setUseMockData(false);
      },
      onFailure: (err) => {
        console.error('Error fetching learning plans:', err);
        setError('Failed to load learning plans from server. Showing sample data instead.');
        
        // Filter mock data based on the current filter
        let filteredMockPlans = MOCK_PLANS;
        if (filter === 'my') {
          // For "my plans" filter, only show the first two mock plans
          filteredMockPlans = MOCK_PLANS.slice(0, 2);
        }
        setPlans(filteredMockPlans);
        setLoading(false);
        setUseMockData(true);
      }
    });
  }, [filter, userId]);

  if (loading) {
    return (
      <div className="learning-plane__loading">
        <div className="learning-plane__spinner" />
      </div>
    );
  }

  // Even if there's an error, we'll show mock data instead of an error screen
  if (error && !useMockData) {
    return (
      <div className={`learning-plane__error learning-plane__card--${theme}`}>
        <p className="learning-plane__error-message">{error}</p>
        <Link to="/learning-plans" className="learning-plane__button learning-plane__button--primary">
          Browse Plans
        </Link>
      </div>
    );
  }

  if (plans.length === 0) {
    return (
      <div className={`learning-plane__empty learning-plane__card--${theme}`}>
        <p className="learning-plane__empty-message">
          {filter === 'my' ? "You haven't created any learning plans yet." : 'No learning plans found.'}
        </p>
        <Link
          to="/create-learning-plan"
          className="learning-plane__button learning-plane__button--primary"
        >
          Create Your First Plan
        </Link>
      </div>
    );
  }

  return (
    <>
      {useMockData && (
        <div className={`learning-plane__warning learning-plane__card--${theme}`}>
          <p className="learning-plane__warning-message">
            {error} <br />
            <small>Please note that this is sample data and changes will not be saved until the server issue is fixed.</small>
          </p>
        </div>
      )}
      
      <div className="learning-plane__grid">
        {plans.map((plan) => (
          <LearningPlanCard key={plan.id} plan={plan} />
        ))}
      </div>
    </>
  );
}; 